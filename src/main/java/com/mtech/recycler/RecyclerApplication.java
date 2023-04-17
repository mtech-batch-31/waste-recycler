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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class RecyclerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecyclerApplication.class, args);
    }


    // uncomment below to insert dummy data in your local db

    @Bean
    public CommandLineRunner demoData(UserRepository userRepository, RecycleCategoryRepository recycleCategoryRepository, PromotionRepository promotionRepository) {
        return args -> {
            userRepository.deleteAll();

            var user = new Customer();
            user.setEmail("test@test.com");
            user.setPassword(Utilities.encodePassword("your-password"));
            user.setRole(Role.CUSTOMER);
            userRepository.save(user);

            recycleCategoryRepository.deleteAll();
            List<RecycleCategory> categories = new ArrayList<>() {{
                add(new RecycleCategory("Electronics", new BigDecimal(700), "item"));
                add(new RecycleCategory("Battery", new BigDecimal(500), "ton", "kg"));
                add(new RecycleCategory("Clothes", new BigDecimal(300), "item", "kg", "g"));
                add(new RecycleCategory("Glass", new BigDecimal(200), "ton", "kg"));
                add(new RecycleCategory("Plastic", new BigDecimal(100), "ton", "kg", "g"));
                add(new RecycleCategory("Paper", new BigDecimal(50), "ton", "sheet"));
            }};

            recycleCategoryRepository.saveAll(categories);

            promotionRepository.deleteAll();
            var discount1 = new Promotion();
            discount1.setPromotionCode("d001");
            discount1.setDescription("Early bird");
            discount1.setPercentage(0.2f);
            discount1.setStartDate(Instant.now().toDateTime().minusDays(1).toDate());
            discount1.setEndDate(Instant.now().toDateTime().plusMonths(1).toDate());

            promotionRepository.save(discount1);
        };
    }

}
