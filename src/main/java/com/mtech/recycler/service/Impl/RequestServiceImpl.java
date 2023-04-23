package com.mtech.recycler.service.Impl;

import com.mtech.recycler.constant.CommonConstant;
import com.mtech.recycler.entity.RecycleItem;
import com.mtech.recycler.helper.Utilities;
import com.mtech.recycler.model.*;
import com.mtech.recycler.repository.PromotionRepository;
import com.mtech.recycler.repository.RecycleCategoryRepository;
import com.mtech.recycler.repository.RecycleItemRepository;
import com.mtech.recycler.service.RequestService;
import com.mtech.recycler.service.pricingstrategy.PromotionCode1PricingStrategy;
import com.mtech.recycler.service.pricingstrategy.PromotionCode2PricingStrategy;
import com.mtech.recycler.service.pricingstrategy.PromotionCode3PricingStrategy;
import com.mtech.recycler.service.pricingstrategy.PromotionPricingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class RequestServiceImpl implements RequestService {

    final private RecycleCategoryRepository recycleCategoryRepository;
    final private PromotionRepository promotionRepository;
    final private RecycleItemRepository recycleItemRepository;

    public RequestServiceImpl(RecycleCategoryRepository recycleCategoryRepository, PromotionRepository promotionRepository, RecycleItemRepository recycleItemRepository) {
        this.recycleCategoryRepository = recycleCategoryRepository;
        this.promotionRepository = promotionRepository;
        this.recycleItemRepository = recycleItemRepository;
    }

    @Override
    public Optional<PricingResponse> getRequestTotalPricing(PricingRequest request) {
        var response = new PricingResponse();
        List<Item> items = new ArrayList<>();

        PromotionPricingStrategy pricingStrategy;
        switch (request.getPromoCode().toLowerCase()) {
            case "d001" -> pricingStrategy = new PromotionCode1PricingStrategy(recycleCategoryRepository, promotionRepository, recycleItemRepository);
            case "d002" -> pricingStrategy = new PromotionCode2PricingStrategy(recycleCategoryRepository, promotionRepository, recycleItemRepository);
            case "d003" -> pricingStrategy = new PromotionCode3PricingStrategy(recycleCategoryRepository, promotionRepository, recycleItemRepository);
            default -> pricingStrategy = new PromotionCode1PricingStrategy(recycleCategoryRepository, promotionRepository, recycleItemRepository);
        }

        BigDecimal totalPrice = pricingStrategy.calculateTotalPrice(request.getData(), request.getPromoCode(), items);
        List<Item> subTotalPrice = pricingStrategy.calculateSubTotalPrice(request.getData(), request.getPromoCode(), items);

        Utilities.mapDescriptions(request.getData(), items);

        log.info("RequestService - GetRequestTotalPricing - total price after promo: %s".formatted(totalPrice));

        response.setTotalPrice(totalPrice);
        response.setItems(subTotalPrice);

        log.info("RequestService - GetRequestTotalPricing - end");

        return Optional.of(response);
    }

    @Override
    public List<Category> GetAllRecycleCategories() {
        return StreamSupport.stream(recycleCategoryRepository.findAll().spliterator(), false).map(r -> new Category(r.getName(), r.getPrice(), 0, r.getUnitOfMeasurement(), "")).toList();
    }

    public Optional<RecycleItem> getRequest(String email, int record) {
        List<RecycleItem> recycleItems = recycleItemRepository.findByEmail(email);
        if (recycleItems.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, CommonConstant.ErrorMessage.NO_RECORD_FOUND);
        }
        else {
            try {
                RecycleItem recycleItem = recycleItems.get(record);
                return Optional.of(recycleItem);
            } catch (IndexOutOfBoundsException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, CommonConstant.ErrorMessage.NO_INDEX_FOUND + record);
            }
        }
    }


    @Override
    public Optional<RecycleResponse> SubmitRequest(RecycleRequest recycleRequest) {
        PricingRequest pricingRequest = Utilities.convertSubmitRequestToPricingRequest(recycleRequest);
        Optional<PricingResponse> pricingResponse = getRequestTotalPricing(pricingRequest);
        RecycleResponse recycleResponse = new RecycleResponse();
        recycleResponse.setReturnCode(CommonConstant.ReturnCode.SUCCESS);
        recycleResponse.setMessage(CommonConstant.Message.SUCCESSFUL_REQUEST);
        recycleResponse.setEmail(recycleRequest.getEmail());
        pricingResponse.ifPresent(response -> recycleResponse.setTotalPrice(response.getTotalPrice()));
        recycleResponse.setCollectionStatus("Pending Approval");
        recycleResponse.setPromoCode(recycleRequest.getPromoCode());
        recycleResponse.setContactPerson(recycleRequest.getContactPerson());
        recycleResponse.setContactNumber(recycleRequest.getContactNumber());
        recycleResponse.setCollectionDate(recycleRequest.getCollectionDate());
        pricingResponse.ifPresent(response -> recycleResponse.setItems(response.getItems()));
        RecycleItem recycleItem = new RecycleItem();
        recycleItem.setEmail(recycleRequest.getEmail());
        recycleItem.setReturnCode(CommonConstant.ReturnCode.SUCCESS);
        recycleItem.setMessage(CommonConstant.Message.SUCCESSFUL_REQUEST);
        recycleItem.setTotalPrice(recycleResponse.getTotalPrice());
        pricingResponse.ifPresent(response -> recycleItem.setDbItems(response.getItems()));
        recycleItem.setCollectionStatus("Pending Approval");
        recycleItem.setPromoCode(recycleRequest.getPromoCode());
        recycleItem.setContactPerson(recycleRequest.getContactPerson());
        recycleItem.setContactNumber(recycleRequest.getContactNumber());
        recycleItem.setCollectionDate(recycleRequest.getCollectionDate());
        recycleItemRepository.save(recycleItem);
        return Optional.of(recycleResponse);
    }

    boolean isWithinRange(Date startDate, Date endDate) {
        Date today = Instant.now().toDate();

        return today.after(startDate) && today.before(endDate);
    }
}
