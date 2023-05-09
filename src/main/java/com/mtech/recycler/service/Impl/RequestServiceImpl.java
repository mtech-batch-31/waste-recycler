package com.mtech.recycler.service.Impl;

import com.mtech.recycler.constant.CommonConstant;
import com.mtech.recycler.entity.RecycleRequest;
import com.mtech.recycler.entity.User;
import com.mtech.recycler.helper.Logger;
import com.mtech.recycler.helper.Utilities;
import com.mtech.recycler.model.*;
import com.mtech.recycler.notification.NotificationChannel;
import com.mtech.recycler.notification.NotificationChannelFactory;
import com.mtech.recycler.notification.RequestNotification;
import com.mtech.recycler.notification.model.NotificationModel;
import com.mtech.recycler.repository.PromotionRepository;
import com.mtech.recycler.repository.RecycleCategoryRepository;
import com.mtech.recycler.repository.RecycleRequestRepository;
import com.mtech.recycler.service.RequestService;
import com.mtech.recycler.service.pricingstrategy.CategoryPricingStrategy;
import com.mtech.recycler.service.pricingstrategy.DayPricingStrategy;
import com.mtech.recycler.service.pricingstrategy.NormalPricingStrategy;
import com.mtech.recycler.service.pricingstrategy.PromotionPricingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.StreamSupport;

@Service
public class RequestServiceImpl implements RequestService {

    private final Logger log = Logger.getInstance();
    final private RecycleCategoryRepository recycleCategoryRepository;
    final private PromotionRepository promotionRepository;
    final private RecycleRequestRepository recycleRequestRepository;

    @Autowired
    private NotificationChannelFactory notifyChannelFactory;

    public RequestServiceImpl(RecycleCategoryRepository recycleCategoryRepository, PromotionRepository promotionRepository
            , RecycleRequestRepository recycleRequestRepository, NotificationChannelFactory notifyChannelFactory) {
        this.recycleCategoryRepository = recycleCategoryRepository;
        this.promotionRepository = promotionRepository;
        this.recycleRequestRepository = recycleRequestRepository;
        this.notifyChannelFactory = notifyChannelFactory;
    }

    @Override
    public Optional<PricingResponse> getRequestTotalPricing(PricingRequest request) {
        var response = new PricingResponse();
        List<Item> items = new ArrayList<>();

        PromotionPricingStrategy pricingStrategy = setPricingStrategy(request);

        try {
            BigDecimal totalPrice = pricingStrategy.calculateTotalPrice(request.getData(), request.getPromoCode(), items);
            List<Item> subTotalPrice = pricingStrategy.calculateSubTotalPrice(request.getData(), request.getPromoCode(), items);

            Utilities.mapDescriptionsFromCategoryToItems(request.getData(), items);

            log.info("RequestService - GetRequestTotalPricing - total price after promo: %s".formatted(totalPrice));

            response.setTotalPrice(totalPrice);
            response.setItems(subTotalPrice);

            log.info("RequestService - GetRequestTotalPricing - end");

            return Optional.of(response);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The following category name is not found: " + e.getMessage());
        }
    }

    private PromotionPricingStrategy setPricingStrategy(PricingRequest request) {
        PromotionPricingStrategy pricingStrategy;
        // Type = Normal
        switch (Objects.requireNonNullElse(request.getPromoCode(), "").toLowerCase()) {
            case "p001" -> pricingStrategy = new NormalPricingStrategy(recycleCategoryRepository, promotionRepository);
            case "p002" -> pricingStrategy = new NormalPricingStrategy(recycleCategoryRepository, promotionRepository);
            case "p003" -> pricingStrategy = new NormalPricingStrategy(recycleCategoryRepository, promotionRepository);
            // Type = Day
            case "earth" -> pricingStrategy = new DayPricingStrategy(recycleCategoryRepository, promotionRepository);
            case "xmas" -> pricingStrategy = new DayPricingStrategy(recycleCategoryRepository, promotionRepository);
            // Type = Category
            case "electronics" -> pricingStrategy = new CategoryPricingStrategy(recycleCategoryRepository, promotionRepository);
            case "glass" -> pricingStrategy = new CategoryPricingStrategy(recycleCategoryRepository, promotionRepository);
            default -> pricingStrategy = new NormalPricingStrategy(recycleCategoryRepository, promotionRepository);
        }
        return pricingStrategy;
    }


    @Override
    public List<Category> getAllRecycleCategories() {
        return StreamSupport.stream(recycleCategoryRepository.findAll().spliterator(), false).map(r -> new Category(r.getName(), r.getPrice(), 0, r.getUnitOfMeasurement(), "")).toList();
    }

    @Override
    public List<RecycleRequest> getRecycleRequests() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        log.info("user: " + user);
        return recycleRequestRepository.findByEmail(user.getEmail());
    }


    @Override
    public Optional<RecycleResponse> submitRequest(com.mtech.recycler.model.RecycleRequest recycleRequest) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        log.info("user: " + user);
        recycleRequest.setEmail(user.getEmail());

        PricingRequest pricingRequest = Utilities.convertRecycleRequestToPricingRequest(recycleRequest);
        Optional<PricingResponse> pricingResponse = getRequestTotalPricing(pricingRequest);

        RecycleResponse recycleResponse = new RecycleResponse();
        recycleResponse.setReturnCode(CommonConstant.ReturnCode.SUCCESS);
        recycleResponse.setMessage(CommonConstant.Message.SUCCESSFUL_REQUEST);
        recycleResponse.setEmail(recycleRequest.getEmail());
        recycleResponse.setCollectionStatus("Pending Approval");
        recycleResponse.setPromoCode(recycleRequest.getPromoCode());
        recycleResponse.setContactPerson(recycleRequest.getContactPerson());
        recycleResponse.setContactNumber(recycleRequest.getContactNumber());
        recycleResponse.setCollectionDate(recycleRequest.getCollectionDate());
        pricingResponse.ifPresent(response -> {
            recycleResponse.setTotalPrice(response.getTotalPrice());
            recycleResponse.setItems(response.getItems());
        });

        RecycleRequest recycleRequestEntity = new RecycleRequest();
        recycleRequestEntity.setEmail(recycleRequest.getEmail());
        recycleRequestEntity.setReturnCode(CommonConstant.ReturnCode.SUCCESS);
        recycleRequestEntity.setMessage(CommonConstant.Message.SUCCESSFUL_REQUEST);
        recycleRequestEntity.setTotalPrice(recycleResponse.getTotalPrice());
        pricingResponse.ifPresent(response -> recycleRequestEntity.setDbItems(response.getItems()));
        recycleRequestEntity.setCollectionStatus("Pending Approval");
        recycleRequestEntity.setPromoCode(recycleRequest.getPromoCode());
        recycleRequestEntity.setContactPerson(recycleRequest.getContactPerson());
        recycleRequestEntity.setContactNumber(recycleRequest.getContactNumber());
        recycleRequestEntity.setCollectionDate(recycleRequest.getCollectionDate());
        recycleRequestRepository.save(recycleRequestEntity);

        if(recycleResponse.getReturnCode().equals(CommonConstant.ReturnCode.SUCCESS)){
            //send email
            NotificationChannel channel = notifyChannelFactory.notificationChannel(NotificationChannelFactory.CHANNEL_TYPE.SMTP);
            RequestNotification requestNotification = new RequestNotification(channel);
            NotificationModel notifModel = new NotificationModel();
            notifModel.setUser(user);
            requestNotification.send(notifModel);
        }
        return Optional.of(recycleResponse);
    }
}

