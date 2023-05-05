package com.mtech.recycler.helper;

import com.mtech.recycler.constant.CommonConstant;
import com.mtech.recycler.entity.Customer;
import com.mtech.recycler.entity.Promotion;
import com.mtech.recycler.entity.RecycleCategory;
import com.mtech.recycler.entity.RecycleRequest;
import com.mtech.recycler.model.Item;
import com.mtech.recycler.model.Role;
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

            var user = new Customer();
            user.setEmail("andrew@mail.com");
            user.setAddress("25 Heng Mui Keng Terrace, Institute of Systems Science, Singapore 119615");
            user.setPostalCode("119615");
            user.setContactNumber("65162093");
            user.setFirstName("Andrew");
            user.setLastName("Tan");
            user.setPassword(Utilities.encodePassword("P@ssw0rd"));
            user.setRole(Role.CUSTOMER);
            customerRepository.save(user);

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

            // Recycle Request
            recycleRequestRepository.deleteAll();

            RecycleRequest recycleRequestEntity = new RecycleRequest();
            recycleRequestEntity.setEmail("andrew@mail.com");
            recycleRequestEntity.setReturnCode(CommonConstant.ReturnCode.SUCCESS);
            recycleRequestEntity.setMessage(CommonConstant.Message.SUCCESSFUL_REQUEST);
            recycleRequestEntity.setTotalPrice(BigDecimal.valueOf(15));
            List<Item> items = new ArrayList<>();
            items.add(new Item("Paper", 5, BigDecimal.ONE, BigDecimal.valueOf(5),  "Newspaper"));
            items.add(new Item("Electronics", 1, BigDecimal.valueOf(10), BigDecimal.valueOf(10),  "iPhone 6"));
            recycleRequestEntity.setDbItems(items);

            recycleRequestEntity.setCollectionStatus("Collected");
            recycleRequestEntity.setPromoCode("p001");
            recycleRequestEntity.setContactPerson("Andrew");
            recycleRequestEntity.setContactNumber("65162093");
            recycleRequestEntity.setCollectionDate("2023-04-30 10:00:00");
            recycleRequestRepository.save(recycleRequestEntity);

            RecycleRequest recycleRequestEntity2 = new RecycleRequest();
            recycleRequestEntity2.setEmail("andrew@mail.com");
            recycleRequestEntity2.setReturnCode(CommonConstant.ReturnCode.SUCCESS);
            recycleRequestEntity2.setMessage(CommonConstant.Message.SUCCESSFUL_REQUEST);
            recycleRequestEntity2.setTotalPrice(BigDecimal.valueOf(12));
            List<Item> items2 = new ArrayList<>();
            items2.add(new Item("Metal", 5, BigDecimal.valueOf(2), BigDecimal.valueOf(10),  "Aluminium drink cans"));
            items2.add(new Item("Plastic", 1, BigDecimal.valueOf(2), BigDecimal.valueOf(2),  "Plastic bottles"));
            recycleRequestEntity2.setDbItems(items2);

            recycleRequestEntity2.setCollectionStatus("Collected");
            recycleRequestEntity2.setContactPerson("Andrew");
            recycleRequestEntity2.setContactNumber("65162093");
            recycleRequestEntity2.setCollectionDate("2023-05-04 18:00:00");
            recycleRequestRepository.save(recycleRequestEntity2);

            RecycleRequest recycleRequestEntity3 = new RecycleRequest();
            recycleRequestEntity3.setEmail("andrew@mail.com");
            recycleRequestEntity3.setReturnCode(CommonConstant.ReturnCode.SUCCESS);
            recycleRequestEntity3.setMessage(CommonConstant.Message.SUCCESSFUL_REQUEST);
            recycleRequestEntity3.setTotalPrice(BigDecimal.valueOf(7));
            List<Item> items3 = new ArrayList<>();
            items3.add(new Item("Glass", 1, BigDecimal.valueOf(3), BigDecimal.valueOf(3),  "Glass bottles"));
            items3.add(new Item("Textile", 2, BigDecimal.valueOf(2), BigDecimal.valueOf(4),  "Tee shirts and jeans"));
            recycleRequestEntity3.setDbItems(items3);

            recycleRequestEntity3.setCollectionStatus("Pending Approval");
            recycleRequestEntity3.setContactPerson("Andrew");
            recycleRequestEntity3.setContactNumber("65162093");
            recycleRequestEntity3.setCollectionDate("2023-05-14 18:00:00");
            recycleRequestRepository.save(recycleRequestEntity3);
        } else {
            log.info("seeding not enabled");
        }
    }


}
