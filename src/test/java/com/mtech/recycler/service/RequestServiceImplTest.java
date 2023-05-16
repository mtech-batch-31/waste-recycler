package com.mtech.recycler.service;

import com.mtech.recycler.constant.CommonConstant;
import com.mtech.recycler.entity.*;
import com.mtech.recycler.entity.RecycleRequest;
import com.mtech.recycler.model.*;
import com.mtech.recycler.notification.NotificationChannel;
import com.mtech.recycler.notification.NotificationChannelFactory;
import com.mtech.recycler.repository.PromotionRepository;
import com.mtech.recycler.repository.RecycleCategoryRepository;
import com.mtech.recycler.repository.RecycleRequestRepository;
import com.mtech.recycler.service.Impl.RequestServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.BeanFactory;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@Slf4j
public class RequestServiceImplTest {

    private RecycleCategoryRepository recycleCategoryRepository;
    private PromotionRepository promotionRepository;

    private RequestService requestService;

    @BeforeEach
    public void init() {
        promotionRepository = Mockito.mock(PromotionRepository.class);
        recycleCategoryRepository = Mockito.mock(RecycleCategoryRepository.class);
        RecycleRequestRepository recycleRequestRepository = Mockito.mock(RecycleRequestRepository.class);
        BeanFactory beanFactory = Mockito.mock(BeanFactory.class);
        NotificationChannelFactory mockNotifyChannelFactory = new NotificationChannelFactory(beanFactory);
        requestService = new RequestServiceImpl(recycleCategoryRepository, promotionRepository, recycleRequestRepository,
                mockNotifyChannelFactory);
    }

    @Test
    void testGetAllCategories_Succeed() {
        int expectedSize = 4;
        Iterable<RecycleCategory> recycleCategories = new ArrayList<>() {{
            add(new RecycleCategory("1", "test", new BigDecimal(1), "kg"));
            add(new RecycleCategory("2", "test2", new BigDecimal(1), "kg"));
            add(new RecycleCategory("3", "test3", new BigDecimal(1), "kg"));
            add(new RecycleCategory("4", "test4", new BigDecimal(1), "kg"));
        }};

        Mockito.when(recycleCategoryRepository.findAll()).thenReturn(recycleCategories);
        List<Category> rc = requestService.getAllRecycleCategories();
        verify(recycleCategoryRepository, Mockito.times(1)).findAll();
        Assertions.assertNotNull(rc);
        Assertions.assertEquals(expectedSize, rc.size());
        Assertions.assertEquals("test", rc.get(0).getCategory());
        Assertions.assertEquals("test2", rc.get(1).getCategory());
    }

    @Test
    void testGetAllCategories_Empty() {
        int expectedSize = 0;
        Iterable<RecycleCategory> recycleCategories = new ArrayList<>();

        Mockito.when(recycleCategoryRepository.findAll()).thenReturn(recycleCategories);
        List<Category> rc = requestService.getAllRecycleCategories();
        verify(recycleCategoryRepository, Mockito.times(1)).findAll();
        Assertions.assertNotNull(rc);
        Assertions.assertEquals(expectedSize, rc.size());
    }

    @Test
    void testGetRequestTotalPricing_WithoutPromotion_Success() {

        var expectedTotalPrice = new BigDecimal("150.0");
        var expectedBatteryTotalPrice = new BigDecimal("100.0");
        var expectedPlasticTotalPrice = new BigDecimal("50.0");
        var categories = new ArrayList<Category>() {{
            add(new Category("Battery", new BigDecimal(0), 10, "", ""));
            add(new Category("Plastic", new BigDecimal(0), 10, "", ""));
        }};

        var batteryRecycle = new RecycleCategory();
        batteryRecycle.setPrice(new BigDecimal(10));

        var plasticRecycle = new RecycleCategory();
        plasticRecycle.setPrice(new BigDecimal(5));

        var pricingRequest = new PricingRequest();
        pricingRequest.setData(categories);

        Mockito.when(recycleCategoryRepository.findByName("Battery")).thenReturn(Optional.of(batteryRecycle));
        Mockito.when(recycleCategoryRepository.findByName("Plastic")).thenReturn(Optional.of(plasticRecycle));

        Optional<PricingResponse> response = requestService.getRequestTotalPricing(pricingRequest);
        verify(recycleCategoryRepository, Mockito.times(2)).findByName(Mockito.any());
        Assertions.assertNotNull(response);
        assertTrue(response.isPresent());
        Assertions.assertNotNull(response.get());
        Assertions.assertEquals(expectedTotalPrice, response.get().getTotalPrice());
        Assertions.assertEquals(expectedBatteryTotalPrice, response.get().getItems().get(0).getSubTotalPrice());
        Assertions.assertEquals(expectedPlasticTotalPrice, response.get().getItems().get(1).getSubTotalPrice());
    }

    @Test
    void testGetRequestTotalPricing_WithPromotion_Success() {
        var expectedTotalPrice = new BigDecimal("165.00");
        var expectedBatteryTotalPrice = new BigDecimal("110.00");
        var expectedPlasticTotalPrice = new BigDecimal("55.00");
        var categories = new ArrayList<Category>() {{
            add(new Category("Battery", new BigDecimal(0), 10, "", ""));
            add(new Category("Plastic", new BigDecimal(0), 10, "", ""));
        }};

        var promotion = new Promotion() {{
            setPromotionCode("p001");
            setStartDate(DateTime.now().minusDays(1).toDate());
            setEndDate(DateTime.now().plusDays(1).toDate());
            setPercentage(0.1);
        }};

        var batteryRecycle = new RecycleCategory();
        batteryRecycle.setPrice(new BigDecimal(10));

        var plasticRecycle = new RecycleCategory();
        plasticRecycle.setPrice(new BigDecimal(5));

        var pricingRequest = new PricingRequest();
        pricingRequest.setPromoCode("p001");
        pricingRequest.setData(categories);

        Mockito.when(recycleCategoryRepository.findByName("Battery")).thenReturn(Optional.of(batteryRecycle));
        Mockito.when(recycleCategoryRepository.findByName("Plastic")).thenReturn(Optional.of(plasticRecycle));
        Mockito.when(promotionRepository.findDiscountByPromotionCode(Mockito.any())).thenReturn(Optional.of(promotion));

        Optional<PricingResponse> response = requestService.getRequestTotalPricing(pricingRequest);
        verify(recycleCategoryRepository, Mockito.times(2)).findByName(Mockito.any());
        Assertions.assertNotNull(response);
        assertTrue(response.isPresent());
        Assertions.assertNotNull(response.get());
        Assertions.assertEquals(expectedTotalPrice, response.get().getTotalPrice());
        Assertions.assertEquals(expectedBatteryTotalPrice, response.get().getItems().get(0).getSubTotalPrice());
        Assertions.assertEquals(expectedPlasticTotalPrice, response.get().getItems().get(1).getSubTotalPrice());
    }

    @Test
    void testGetRequestTotalPricing_WithCategoryStrategy_Success() {
        var expectedTotalPrice = new BigDecimal("24.0");
        var expectedBatteryTotalPrice = new BigDecimal("4.00");
        var expectedPlasticTotalPrice = new BigDecimal("20.00");
        var categories = new ArrayList<Category>() {{
            add(new Category("Metal", new BigDecimal(0), 2, "", ""));
            add(new Category("Electronics", new BigDecimal(0), 2, "", ""));
        }};

        var promotion = new Promotion() {{
            setPromotionCode("electronics");
            setStartDate(DateTime.now().minusDays(1).toDate());
            setEndDate(DateTime.now().plusDays(1).toDate());
            setPercentage(0.1);
        }};

        var batteryRecycle = new RecycleCategory();
        batteryRecycle.setPrice(new BigDecimal(2));

        var plasticRecycle = new RecycleCategory();
        plasticRecycle.setPrice(new BigDecimal(10));

        var pricingRequest = new PricingRequest();
        pricingRequest.setPromoCode("electronics");
        pricingRequest.setData(categories);

        Mockito.when(recycleCategoryRepository.findByName("Metal")).thenReturn(Optional.of(batteryRecycle));
        Mockito.when(recycleCategoryRepository.findByName("Electronics")).thenReturn(Optional.of(plasticRecycle));
        Mockito.when(promotionRepository.findDiscountByPromotionCode(Mockito.any())).thenReturn(Optional.of(promotion));

        Optional<PricingResponse> response = requestService.getRequestTotalPricing(pricingRequest);
        verify(recycleCategoryRepository, Mockito.times(2)).findByName(Mockito.any());
        Assertions.assertNotNull(response);
        assertTrue(response.isPresent());
        Assertions.assertNotNull(response.get());
        Assertions.assertEquals(expectedTotalPrice, response.get().getTotalPrice());
        Assertions.assertEquals(expectedBatteryTotalPrice, response.get().getItems().get(0).getSubTotalPrice());
        Assertions.assertEquals(expectedPlasticTotalPrice, response.get().getItems().get(1).getSubTotalPrice());
    }

    @Test
    void testGetRequestTotalPricing_WithDayStrategy_Success() {
        var expectedTotalPrice = new BigDecimal("33.600");
        var expectedBatteryTotalPrice = new BigDecimal("5.60");
        var expectedPlasticTotalPrice = new BigDecimal("28.00");
        var categories = new ArrayList<Category>() {{
            add(new Category("Metal", new BigDecimal(0), 2, "", ""));
            add(new Category("Electronics", new BigDecimal(0), 2, "", ""));
        }};

        var promotion = new Promotion() {{
            setPromotionCode("earth");
            setStartDate(DateTime.now().minusDays(1).toDate());
            setEndDate(DateTime.now().plusDays(1).toDate());
            setPercentage(0.4);
        }};

        var batteryRecycle = new RecycleCategory();
        batteryRecycle.setPrice(new BigDecimal(2));

        var plasticRecycle = new RecycleCategory();
        plasticRecycle.setPrice(new BigDecimal(10));

        var pricingRequest = new PricingRequest();
        pricingRequest.setPromoCode("earth");
        pricingRequest.setData(categories);

        Mockito.when(recycleCategoryRepository.findByName("Metal")).thenReturn(Optional.of(batteryRecycle));
        Mockito.when(recycleCategoryRepository.findByName("Electronics")).thenReturn(Optional.of(plasticRecycle));
        Mockito.when(promotionRepository.findDiscountByPromotionCode(Mockito.any())).thenReturn(Optional.of(promotion));

        Optional<PricingResponse> response = requestService.getRequestTotalPricing(pricingRequest);
        verify(recycleCategoryRepository, Mockito.times(2)).findByName(Mockito.any());
        Assertions.assertNotNull(response);
        assertTrue(response.isPresent());
        Assertions.assertNotNull(response.get());
        Assertions.assertEquals(expectedTotalPrice, response.get().getTotalPrice());
        Assertions.assertEquals(expectedBatteryTotalPrice, response.get().getItems().get(0).getSubTotalPrice());
        Assertions.assertEquals(expectedPlasticTotalPrice, response.get().getItems().get(1).getSubTotalPrice());
    }
    @Test
    void testGetRequestTotalPricing_ThrowExceptionWhenPromoCodeNotFound() {
        var categories = new ArrayList<Category>() {{
            add(new Category("Battery", new BigDecimal(0), 10, "", ""));
            add(new Category("Plastic", new BigDecimal(0), 10, "", ""));
        }};

        var batteryRecycle = new RecycleCategory();
        batteryRecycle.setPrice(new BigDecimal(10));

        var plasticRecycle = new RecycleCategory();
        plasticRecycle.setPrice(new BigDecimal(5));

        var pricingRequest = new PricingRequest();
        pricingRequest.setPromoCode("p001");
        pricingRequest.setData(categories);

        Mockito.when(recycleCategoryRepository.findByName("Battery")).thenReturn(Optional.of(batteryRecycle));
        Mockito.when(recycleCategoryRepository.findByName("Plastic")).thenReturn(Optional.of(plasticRecycle));
        assertThrows(ResponseStatusException.class, () -> {
            Optional<PricingResponse> response = requestService.getRequestTotalPricing(pricingRequest);

            Assertions.assertNull(response);
        });
    }

    @Test
    void testGetRequestTotalPricing_ThrowExceptionWhenCategoryNotFound() {
        var categories = new ArrayList<Category>() {{
            add(new Category("Battery", new BigDecimal(0), 10, "", ""));
            add(new Category("Plastic", new BigDecimal(0), 10, "", ""));
        }};

        var pricingRequest = new PricingRequest();
        pricingRequest.setData(categories);

        Mockito.when(recycleCategoryRepository.findByName(Mockito.any())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            Optional<PricingResponse> response = requestService.getRequestTotalPricing(pricingRequest);
            Assertions.assertNull(response);
        });
    }

    @Test
    void testGetRequestTotalPricing_WithPromotionIsExpired_Success() {
        var categories = new ArrayList<Category>() {{
            add(new Category("Battery", new BigDecimal(0), 10, "", ""));
            add(new Category("Plastic", new BigDecimal(0), 10, "", ""));
        }};

        var promotion = new Promotion() {{
            setPromotionCode("p001");
            setStartDate(DateTime.now().minusDays(1).toDate());
            setEndDate(DateTime.now().toDate());
            setPercentage(0.1);
        }};

        var batteryRecycle = new RecycleCategory();
        batteryRecycle.setPrice(new BigDecimal(10));

        var plasticRecycle = new RecycleCategory();
        plasticRecycle.setPrice(new BigDecimal(5));

        var pricingRequest = new PricingRequest();
        pricingRequest.setPromoCode("p001");
        pricingRequest.setData(categories);

        Mockito.when(recycleCategoryRepository.findByName("Battery")).thenReturn(Optional.of(batteryRecycle));
        Mockito.when(recycleCategoryRepository.findByName("Plastic")).thenReturn(Optional.of(plasticRecycle));
        Mockito.when(promotionRepository.findDiscountByPromotionCode(Mockito.any())).thenReturn(Optional.of(promotion));

        assertThrows(ResponseStatusException.class, () -> {
            Optional<PricingResponse> response = requestService.getRequestTotalPricing(pricingRequest);
            verify(recycleCategoryRepository, Mockito.times(2)).findByName(Mockito.any());
            Assertions.assertNull(response);
        });
    }

    @Test
    void testGetRequestTotalPricing_WithDayStrategyIsExpired_Success() {
        var categories = new ArrayList<Category>() {{
            add(new Category("Metal", new BigDecimal(0), 2, "", ""));
            add(new Category("Electronics", new BigDecimal(0), 2, "", ""));
        }};

        var promotion = new Promotion() {{
            setPromotionCode("xmas");
            setStartDate(DateTime.now().minusDays(1).toDate());
            setEndDate(DateTime.now().toDate());
            setPercentage(0.4);
        }};

        var batteryRecycle = new RecycleCategory();
        batteryRecycle.setPrice(new BigDecimal(2));

        var plasticRecycle = new RecycleCategory();
        plasticRecycle.setPrice(new BigDecimal(10));

        var pricingRequest = new PricingRequest();
        pricingRequest.setPromoCode("xmas");
        pricingRequest.setData(categories);

        Mockito.when(recycleCategoryRepository.findByName("Metal")).thenReturn(Optional.of(batteryRecycle));
        Mockito.when(recycleCategoryRepository.findByName("Electronics")).thenReturn(Optional.of(plasticRecycle));
        Mockito.when(promotionRepository.findDiscountByPromotionCode(Mockito.any())).thenReturn(Optional.of(promotion));

        assertThrows(ResponseStatusException.class, () -> {
            Optional<PricingResponse> response = requestService.getRequestTotalPricing(pricingRequest);
            verify(recycleCategoryRepository, Mockito.times(2)).findByName(Mockito.any());
            Assertions.assertNull(response);
        });
    }

    @Test
    void testGetRequestTotalPricing_WithCategoryStrategyIsExpired_Success() {
        var categories = new ArrayList<Category>() {{
            add(new Category("Metal", new BigDecimal(0), 2, "", ""));
            add(new Category("Electronics", new BigDecimal(0), 2, "", ""));
        }};

        var promotion = new Promotion() {{
            setPromotionCode("glass");
            setStartDate(DateTime.now().minusDays(1).toDate());
            setEndDate(DateTime.now().toDate());
            setPercentage(0.4);
        }};

        var batteryRecycle = new RecycleCategory();
        batteryRecycle.setPrice(new BigDecimal(2));

        var plasticRecycle = new RecycleCategory();
        plasticRecycle.setPrice(new BigDecimal(10));

        var pricingRequest = new PricingRequest();
        pricingRequest.setPromoCode("glass");
        pricingRequest.setData(categories);

        Mockito.when(recycleCategoryRepository.findByName("Metal")).thenReturn(Optional.of(batteryRecycle));
        Mockito.when(recycleCategoryRepository.findByName("Electronics")).thenReturn(Optional.of(plasticRecycle));
        Mockito.when(promotionRepository.findDiscountByPromotionCode(Mockito.any())).thenReturn(Optional.of(promotion));

        assertThrows(ResponseStatusException.class, () ->
                requestService.getRequestTotalPricing(pricingRequest)
        );
    }

    @Test
    void testSubmitRequest_Success() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        User user = Mockito.mock(User.class);
        RecycleRequestDto recycleRequestDto = new RecycleRequestDto();

        recycleRequestDto.setEmail("andrew@mail.com");
        recycleRequestDto.setReturnCode(CommonConstant.ReturnCode.SUCCESS);
        recycleRequestDto.setData(Collections.singletonList(new Category("Electronics", new BigDecimal(10), 2, "piece", "Electronics")));
        recycleRequestDto.setCollectionStatus("Pending Approval");
        recycleRequestDto.setPromoCode("electronics");
        recycleRequestDto.setContactPerson("Andrew");
        recycleRequestDto.setContactNumber("83930521");
        recycleRequestDto.setCollectionDate("2023-05-04 18:00:00");
        recycleRequestDto.setReturnCode(CommonConstant.ReturnCode.SUCCESS);
        recycleRequestDto.setCollectionStatus("Pending Approval");

        RecycleResponse expectedResponse = new RecycleResponse();

        expectedResponse.setItems(Collections.singletonList(new Item("Electronics",
                 2,
                 new BigDecimal("10"),
                 new BigDecimal("22.00"),
                "Electronics")));
        expectedResponse.setReturnCode("00");
        expectedResponse.setEmail("andrew@mail.com");
        expectedResponse.setMessage("The request has been successfully processed");
        expectedResponse.setTotalPrice(new BigDecimal("22.00"));
        expectedResponse.setCollectionStatus("Pending Approval");
        expectedResponse.setPromoCode("electronics");
        expectedResponse.setContactPerson("Andrew");
        expectedResponse.setContactNumber("83930521");
        expectedResponse.setCollectionDate("2023-05-04 18:00:00");

        RecycleRequest recycleRequestEntity = new RecycleRequest();
        RecycleRequestRepository recycleRequestRepository = Mockito.mock(RecycleRequestRepository.class);

        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        Mockito.when(user.getEmail()).thenReturn("andrew@mail.com");
        Mockito.when(recycleRequestRepository.save(Mockito.any())).thenReturn(recycleRequestEntity);

        Promotion promotion = new Promotion();
        promotion.setId("1");
        promotion.setPromotionCode("electronics");
        promotion.setDescription("Electronics");
        promotion.setStartDate(DateTime.now().minusDays(1).toDate());
        promotion.setEndDate(DateTime.now().plusDays(1).toDate());
        promotion.setPercentage(0.1);
        Mockito.when(promotionRepository.findDiscountByPromotionCode(Mockito.anyString()))
                .thenReturn(Optional.of(promotion));

        RecycleCategoryRepository recycleCategoryRepository = Mockito.mock(RecycleCategoryRepository.class);
        RecycleCategory recycleCategory = new RecycleCategory("Electronics", BigDecimal.valueOf(10), "Piece");

        Mockito.when(recycleCategoryRepository.findByName("Electronics"))
                .thenReturn(Optional.of(recycleCategory));

        NotificationChannelFactory notifyChannelFactory = Mockito.mock(NotificationChannelFactory.class);
        NotificationChannel notificationChannel = Mockito.mock(NotificationChannel.class);
        Mockito.when(notifyChannelFactory.notificationChannel(NotificationChannelFactory.CHANNEL_TYPE.SMTP))
                .thenReturn(notificationChannel);

        BeanFactory beanFactory = Mockito.mock(BeanFactory.class);
        NotificationChannelFactory mockNotifyChannelFactory = new NotificationChannelFactory(beanFactory);

        Mockito.when(mockNotifyChannelFactory.notificationChannel(NotificationChannelFactory.CHANNEL_TYPE.SMTP))
                .thenReturn(notificationChannel);

        requestService = new RequestServiceImpl(
                recycleCategoryRepository,
                promotionRepository,
                recycleRequestRepository,
                mockNotifyChannelFactory
        );

        Mockito.when(notifyChannelFactory.notificationChannel(NotificationChannelFactory.CHANNEL_TYPE.SMTP))
                .thenReturn(notificationChannel);

        Optional<RecycleResponse> actualResponse = requestService.submitRequest(recycleRequestDto);

        Assertions.assertTrue(actualResponse.isPresent());
        Assertions.assertEquals(expectedResponse, actualResponse.get());

        Mockito.verify(securityContext).getAuthentication();
        Mockito.verify(authentication).getPrincipal();
//        Mockito.verify(Utilities.convertRecycleRequestToPricingRequest(recycleRequest));
//        Mockito.verify(requestService).getRequestTotalPricing(pricingRequest);
//        Mockito.verify(recycleRequestRepository).save(recycleRequestEntity);
//        Mockito.verify(requestNotification).send(Mockito.any(NotificationModel.class));
    }

    @Test
    void testGetRecycleRequests_Success() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        User user = new Customer();
        user.setEmail("test@mail.com");
        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        List<RecycleRequest> recycleItems = requestService.getRecycleRequests();
        log.info("reycleItems: {}", recycleItems);
        Assertions.assertNotNull(recycleItems);
    }

}
