package com.mtech.recycler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RecyclerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecyclerApplication.class, args);
    }


// uncomment below to insert dummy data in your local db
//    @Bean
//    public CommandLineRunner demoData(UserRepository repo) {
//        return args -> {
//            repo.deleteAll();
//
//            var user = new User();
//            user.setEmail("test@test.com");
//            user.setPassword("$2a$10$DRQjFI9xuB.8uC37RldSN.nXxXNY1wenDASR7jyuMphat8pz3D9aC");
//            user.setRole(Role.CUSTOMER);
//            repo.save(user);
//        };
//    }
}
