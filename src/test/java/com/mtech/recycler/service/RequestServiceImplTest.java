package com.mtech.recycler.service;

import com.mtech.recycler.entity.Promotion;
import com.mtech.recycler.entity.RecycleCategory;
import com.mtech.recycler.model.*;
import com.mtech.recycler.repository.PromotionRepository;
import com.mtech.recycler.repository.RecycleCategoryRepository;
import com.mtech.recycler.repository.RecycleItemRepository;
import com.mtech.recycler.service.Impl.RequestServiceImpl;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

public class RequestServiceImplTest {

    private RecycleCategoryRepository recycleCategoryRepository;
    private PromotionRepository promotionRepository;

    private RequestService requestService;

    @BeforeEach
    public void init() {
        promotionRepository = Mockito.mock(PromotionRepository.class);
        recycleCategoryRepository = Mockito.mock(RecycleCategoryRepository.class);
        RecycleItemRepository recycleItemRepository = Mockito.mock(RecycleItemRepository.class);
        requestService = new RequestServiceImpl(recycleCategoryRepository, promotionRepository, recycleItemRepository);
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
        List<Category> rc = requestService.GetAllRecycleCategories();
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
        List<Category> rc = requestService.GetAllRecycleCategories();
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
        var expectedBatteryTotalPrice = new BigDecimal("100.0");
        var expectedPlasticTotalPrice = new BigDecimal("50.0");
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
        Assertions.assertThrows(ResponseStatusException.class, () -> {
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
        Assertions.assertThrows(ResponseStatusException.class, () -> {
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

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            Optional<PricingResponse> response = requestService.getRequestTotalPricing(pricingRequest);
            verify(recycleCategoryRepository, Mockito.times(2)).findByName(Mockito.any());
            Assertions.assertNull(response);
        });
    }
}
