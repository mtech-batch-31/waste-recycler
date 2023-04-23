package com.mtech.recycler.service.Impl;

import com.mtech.recycler.constant.CommonConstant;
import com.mtech.recycler.entity.Promotion;
import com.mtech.recycler.entity.RecycleItem;
import com.mtech.recycler.helper.Utilities;
import com.mtech.recycler.model.*;
import com.mtech.recycler.repository.PromotionRepository;
import com.mtech.recycler.repository.RecycleCategoryRepository;
import com.mtech.recycler.repository.RecycleItemRepository;
import com.mtech.recycler.service.RequestService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    public Optional<PricingResponse> GetRequestTotalPricing(PricingRequest request) {
        log.info("RequestService - GetRequestTotalPricing - start");
        var response = new PricingResponse();
        List<Item> items = new ArrayList<>();

        BigDecimal totalPrice = CalculateTotalPrice(request.getData(), request.getPromoCode(), items);

        Utilities.mapDescriptions(request.getData(), items);

        log.info("RequestService - GetRequestTotalPricing - total price after promo: %s".formatted(totalPrice));

        response.setTotalPrice(totalPrice);
        response.setItems(items);

        log.info("RequestService - GetRequestTotalPricing - end");

        return Optional.of(response);
    }

    private BigDecimal CalculateTotalPrice(List<Category> categories, String promoCode, List<Item> items) {
        BigDecimal totalPrice = categories.stream().map(c -> {
            BigDecimal unitPrice = recycleCategoryRepository.findByName(c.getCategory())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The following category name (%s) is not found".formatted(c.getCategory())))
                    .getPrice();
            BigDecimal subTotalPrice = unitPrice.multiply(BigDecimal.valueOf(c.getQuantity()));
            items.add(new Item(c.getCategory(), c.getQuantity(), unitPrice, subTotalPrice, ""));
            return subTotalPrice;
        }).reduce(BigDecimal.ZERO, BigDecimal::add);

        log.info("RequestService - GetRequestTotalPricing - total price before promo: %s".formatted(totalPrice));

        if (StringUtils.hasText(promoCode)) {
            Promotion promotion = promotionRepository.findDiscountByPromotionCode(promoCode)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CommonConstant.ErrorMessage.INVALID_PROMOTION_CODE));

            if (!isWithinRange(promotion.getStartDate(), promotion.getEndDate())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, CommonConstant.ErrorMessage.EXPIRED_PROMOTION_CODE);
            }

            totalPrice = totalPrice.add(totalPrice.multiply(BigDecimal.valueOf(promotion.getPercentage()))).setScale(2, RoundingMode.CEILING);
        }
        return totalPrice;
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
        Optional<PricingResponse> pricingResponse = GetRequestTotalPricing(pricingRequest);
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
