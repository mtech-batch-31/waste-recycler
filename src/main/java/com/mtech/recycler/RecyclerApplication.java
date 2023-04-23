package com.mtech.recycler;

import com.mtech.recycler.entity.Customer;
import com.mtech.recycler.entity.Promotion;
import com.mtech.recycler.entity.RecycleCategory;
import com.mtech.recycler.helper.Utilities;
import com.mtech.recycler.model.Role;
import com.mtech.recycler.repository.PromotionRepository;
import com.mtech.recycler.repository.RecycleCategoryRepository;
import com.mtech.recycler.repository.UserRepository;
import org.joda.time.Instant;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;

@SpringBootApplication
public class RecyclerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecyclerApplication.class, args);
    }


    // uncomment below to insert dummy data in your local db

//        @Bean
//        public CommandLineRunner demoData(UserRepository userRepository, RecycleCategoryRepository recycleCategoryRepository, PromotionRepository promotionRepository) {
//            return args -> {
//                userRepository.deleteAll();
//
//                var user = new Customer();
//                user.setEmail("test@test.com");
//                user.setPassword(Utilities.encodePassword("your-password"));
//                user.setRole(Role.CUSTOMER);
//                userRepository.save(user);
//
//                recycleCategoryRepository.deleteAll();
//                ArrayList<RecycleCategory> categories = new ArrayList<>() {{
//                    add(new RecycleCategory("Electronics", new BigDecimal(700), "item"));
//                    add(new RecycleCategory("Battery", new BigDecimal(500), "kg"));
//                    add(new RecycleCategory("Clothes", new BigDecimal(300), "g"));
//                    add(new RecycleCategory("Glass", new BigDecimal(200), "ton"));
//                    add(new RecycleCategory("Plastic", new BigDecimal(100), "g"));
//                    add(new RecycleCategory("Paper", new BigDecimal(50), "ton"));
//                }};
//
//                recycleCategoryRepository.saveAll(categories);
//
//                promotionRepository.deleteAll();
//                var promotion1 = new Promotion();
//                promotion1.setPromotionCode("p001");
//                promotion1.setDescription("Early bird");
//                promotion1.setPercentage(0.1);
//                promotion1.setStartDate(Instant.now().toDateTime().minusDays(1).toDate());
//                promotion1.setEndDate(Instant.now().toDateTime().plusMonths(1).toDate());
//                promotionRepository.save(promotion1);
//
//                var promotion2 = new Promotion();
//                promotion2.setPromotionCode("p002");
//                promotion2.setDescription("Early bird");
//                promotion2.setPercentage(0.2);
//                promotion2.setStartDate(Instant.now().toDateTime().minusDays(1).toDate());
//                promotion2.setEndDate(Instant.now().toDateTime().plusMonths(1).toDate());
//                promotionRepository.save(promotion2);
//
//                var promotion3 = new Promotion();
//                promotion3.setPromotionCode("p003");
//                promotion3.setDescription("Early bird");
//                promotion3.setPercentage(0.3);
//                promotion3.setStartDate(Instant.now().toDateTime().minusDays(1).toDate());
//                promotion3.setEndDate(Instant.now().toDateTime().plusMonths(1).toDate());
//                promotionRepository.save(promotion3);
//            };
//        }

}
