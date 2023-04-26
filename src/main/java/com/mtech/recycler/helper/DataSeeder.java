package com.mtech.recycler.helper;

import com.mtech.recycler.entity.Customer;
import com.mtech.recycler.entity.Promotion;
import com.mtech.recycler.entity.RecycleCategory;
import com.mtech.recycler.model.Role;
import com.mtech.recycler.repository.CustomerRepository;
import com.mtech.recycler.repository.PromotionRepository;
import com.mtech.recycler.repository.RecycleCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.stream.StreamSupport;


// exclude this class from test coverage, code scans, and reports
@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    public final CustomerRepository customerRepository;
    public final RecycleCategoryRepository recycleCategoryRepository;
    public final PromotionRepository promotionRepository;

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
            user.setEmail("test@mail.com");
            user.setPassword(Utilities.encodePassword("P@ssw0rd"));
            user.setRole(Role.CUSTOMER);
            customerRepository.save(user);

            recycleCategoryRepository.deleteAll();
            ArrayList<RecycleCategory> categories = new ArrayList<>() {{
                add(new RecycleCategory("Electronics", new BigDecimal(700), "item"));
                add(new RecycleCategory("Battery", new BigDecimal(500), "kg"));
                add(new RecycleCategory("Clothes", new BigDecimal(300), "g"));
                add(new RecycleCategory("Glass", new BigDecimal(200), "ton"));
                add(new RecycleCategory("Plastic", new BigDecimal(100), "g"));
                add(new RecycleCategory("Paper", new BigDecimal(50), "ton"));
            }};

            recycleCategoryRepository.saveAll(categories);
            StreamSupport.stream(recycleCategoryRepository.findAll().spliterator(), false).forEach(
                    x -> log.info(String.valueOf(x))
            );

            log.info("find all {}" + recycleCategoryRepository.findAll()  );
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
        } else {
            log.info("seeding not enabled");
        }
    }


}
