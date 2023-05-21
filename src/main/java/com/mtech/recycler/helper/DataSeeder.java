package com.mtech.recycler.helper;

import com.mtech.recycler.constant.CommonConstant;
import com.mtech.recycler.entity.Customer;
import com.mtech.recycler.entity.Promotion;
import com.mtech.recycler.entity.RecycleCategory;
import com.mtech.recycler.entity.RecycleRequest;
import com.mtech.recycler.dto.ItemDto;
import com.mtech.recycler.dto.Role;
import com.mtech.recycler.repository.CustomerRepository;
import com.mtech.recycler.repository.PromotionRepository;
import com.mtech.recycler.repository.RecycleCategoryRepository;
import com.mtech.recycler.repository.RecycleRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;


// exclude this class from test coverage, code scans, and reports
@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    public final CustomerRepository customerRepository;
    public final RecycleCategoryRepository recycleCategoryRepository;
    public final PromotionRepository promotionRepository;
    public final RecycleRequestRepository recycleRequestRepository;

    @Value("${com.mtech.recycler.seed:false}")
    private boolean seedEnabled;

    @Override
    public void run(String... args) throws Exception {
        seed();
    }

    public void seed(){
        log.info("seeding");
        if(seedEnabled){
            log.info("seedEnabled");
            customerRepository.deleteAll();

            // USERS
            var user = new Customer();
            user.setEmail("ernest@mail.com");
            user.setAddress("25 Heng Mui Keng Terrace, Institute of Systems Science, Singapore 119615");
            user.setPostalCode("119615");
            user.setContactNumber("65162093");
            user.setFirstName("Ernest");
            user.setLastName("Lee");
            user.setPassword(Utilities.encodePassword("P@ssw0rd"));
            user.setRole(Role.CUSTOMER);
            customerRepository.save(user);

            var user2 = new Customer();
            user2.setEmail("ray@mail.com");
            user2.setAddress("25 Heng Mui Keng Terrace, Institute of Systems Science, Singapore 119615");
            user2.setPostalCode("119615");
            user2.setContactNumber("65162093");
            user2.setFirstName("Raymond");
            user2.setLastName("Htet");
            user2.setPassword(Utilities.encodePassword("P@ssw0rd"));
            user2.setRole(Role.CUSTOMER);
            customerRepository.save(user2);

            var user3 = new Customer();
            user3.setEmail("andrew@mail.com");
            user3.setAddress("25 Heng Mui Keng Terrace, Institute of Systems Science, Singapore 119615");
            user3.setPostalCode("119615");
            user3.setContactNumber("6583930521");
            user3.setFirstName("Andrew");
            user3.setLastName("Tan");
            user3.setPassword(Utilities.encodePassword("P@ssw0rd"));
            user3.setRole(Role.CUSTOMER);
            customerRepository.save(user3);

            // RECYCLE CATEGORIES
            recycleCategoryRepository.deleteAll();
            ArrayList<RecycleCategory> categories = new ArrayList<>() {{
                add(new RecycleCategory("Electronics", new BigDecimal(10), "piece"));
                add(new RecycleCategory("Glass", new BigDecimal(3), "kg"));
                add(new RecycleCategory("Metal", new BigDecimal(2), "kg"));
                add(new RecycleCategory("Paper", new BigDecimal(1), "kg"));
                add(new RecycleCategory("Plastic", new BigDecimal(2), "kg"));
                add(new RecycleCategory("Textile", new BigDecimal(2), "kg"));
            }};
//            https://blog.moneysmart.sg/budgeting/make-money-recycling/

            recycleCategoryRepository.saveAll(categories);
            StreamSupport.stream(recycleCategoryRepository.findAll().spliterator(), false).forEach(
                    x -> log.info(String.valueOf(x))
            );

            // PROMO CODE
            promotionRepository.deleteAll();
            var promotion1 = new Promotion();
            promotion1.setPromotionCode("p001");
            promotion1.setDescription("Early bird");
            promotion1.setPercentage(0.1);
            promotion1.setStartDate(Instant.now().toDateTime().minusDays(1).toDate());
            promotion1.setEndDate(Instant.now().toDateTime().plusMonths(1).toDate());
            promotionRepository.save(promotion1);

            var promotion2 = new Promotion();
            promotion2.setPromotionCode("p002");
            promotion2.setDescription("Early bird");
            promotion2.setPercentage(0.2);
            promotion2.setStartDate(Instant.now().toDateTime().minusDays(1).toDate());
            promotion2.setEndDate(Instant.now().toDateTime().plusMonths(1).toDate());
            promotionRepository.save(promotion2);

            var promotion3 = new Promotion();
            promotion3.setPromotionCode("p003");
            promotion3.setDescription("Early bird");
            promotion3.setPercentage(0.3);
            promotion3.setStartDate(Instant.now().toDateTime().minusDays(1).toDate());
            promotion3.setEndDate(Instant.now().toDateTime().plusMonths(1).toDate());
            promotionRepository.save(promotion3);

            var promotion4 = new Promotion();
            promotion4.setPromotionCode("electronics");
            promotion4.setDescription("Electronics");
            promotion4.setPercentage(0.1);
            promotion4.setStartDate(Instant.now().toDateTime().minusDays(1).toDate());
            promotion4.setEndDate(Instant.now().toDateTime().plusMonths(1).toDate());
            promotionRepository.save(promotion4);

            var promotion5 = new Promotion();
            promotion5.setPromotionCode("glass");
            promotion5.setDescription("Glass");
            promotion5.setPercentage(0.1);
            promotion5.setStartDate(Instant.now().toDateTime().minusDays(1).toDate());
            promotion5.setEndDate(Instant.now().toDateTime().plusMonths(1).toDate());
            promotionRepository.save(promotion5);

            var promotion6 = new Promotion();
            promotion6.setPromotionCode("earth");
            promotion6.setDescription("Earth");
            promotion6.setPercentage(0.4);
            promotion6.setStartDate(Instant.now().toDateTime().minusDays(1).toDate());
            promotion6.setEndDate(Instant.now().toDateTime().plusMonths(1).toDate());
            promotionRepository.save(promotion6);

            var promotion7 = new Promotion();
            promotion7.setPromotionCode("xmas");
            promotion7.setDescription("Christmas");
            promotion7.setPercentage(0.4);
            promotion7.setStartDate(Instant.now().toDateTime().minusDays(2).toDate());
            promotion7.setEndDate(Instant.now().toDateTime().minusMonths(1).toDate());
            promotionRepository.save(promotion7);

            // RECYCLE REQUESTS
            recycleRequestRepository.deleteAll();

            RecycleRequest recycleRequestEntity = new RecycleRequest();
            recycleRequestEntity.setEmail("ernest@mail.com");
            recycleRequestEntity.setReturnCode(CommonConstant.ReturnCode.SUCCESS);
            recycleRequestEntity.setMessage(CommonConstant.Message.SUCCESSFUL_REQUEST);
            recycleRequestEntity.setTotalPrice(BigDecimal.valueOf(15));
            List<ItemDto> itemDtos = new ArrayList<>();
            itemDtos.add(new ItemDto("Paper", 5, BigDecimal.ONE, BigDecimal.valueOf(5),  "Newspaper"));
            itemDtos.add(new ItemDto("Electronics", 1, BigDecimal.valueOf(10), BigDecimal.valueOf(10),  "iPhone 6"));
            recycleRequestEntity.setDbItems(itemDtos);

            recycleRequestEntity.setCollectionStatus("Collected");
            recycleRequestEntity.setPromoCode("p001");
            recycleRequestEntity.setContactPerson("Ernest");
            recycleRequestEntity.setContactNumber("65162093");
            recycleRequestEntity.setCollectionDate("2023-04-30 10:00:00");
            recycleRequestRepository.save(recycleRequestEntity);

            RecycleRequest recycleRequestEntity2 = new RecycleRequest();
            recycleRequestEntity2.setEmail("ernest@mail.com");
            recycleRequestEntity2.setReturnCode(CommonConstant.ReturnCode.SUCCESS);
            recycleRequestEntity2.setMessage(CommonConstant.Message.SUCCESSFUL_REQUEST);
            recycleRequestEntity2.setTotalPrice(BigDecimal.valueOf(12));
            List<ItemDto> items2 = new ArrayList<>();
            items2.add(new ItemDto("Metal", 5, BigDecimal.valueOf(2), BigDecimal.valueOf(10),  "Aluminium drink cans"));
            items2.add(new ItemDto("Plastic", 1, BigDecimal.valueOf(2), BigDecimal.valueOf(2),  "Plastic bottles"));
            recycleRequestEntity2.setDbItems(items2);

            recycleRequestEntity2.setCollectionStatus("Collected");
            recycleRequestEntity2.setContactPerson("Ernest");
            recycleRequestEntity2.setContactNumber("65162093");
            recycleRequestEntity2.setCollectionDate("2023-05-04 18:00:00");
            recycleRequestRepository.save(recycleRequestEntity2);

            RecycleRequest recycleRequestEntity3 = new RecycleRequest();
            recycleRequestEntity3.setEmail("ernest@mail.com");
            recycleRequestEntity3.setReturnCode(CommonConstant.ReturnCode.SUCCESS);
            recycleRequestEntity3.setMessage(CommonConstant.Message.SUCCESSFUL_REQUEST);
            recycleRequestEntity3.setTotalPrice(BigDecimal.valueOf(7));
            List<ItemDto> items3 = new ArrayList<>();
            items3.add(new ItemDto("Glass", 1, BigDecimal.valueOf(3), BigDecimal.valueOf(3),  "Glass bottles"));
            items3.add(new ItemDto("Textile", 2, BigDecimal.valueOf(2), BigDecimal.valueOf(4),  "Tee shirts and jeans"));
            recycleRequestEntity3.setDbItems(items3);

            recycleRequestEntity3.setCollectionStatus("Pending Approval");
            recycleRequestEntity3.setContactPerson("Ernest");
            recycleRequestEntity3.setContactNumber("65162093");
            recycleRequestEntity3.setCollectionDate("2023-05-14 18:00:00");
            recycleRequestRepository.save(recycleRequestEntity3);
        } else {
            log.info("seeding not enabled");
        }
    }


}
