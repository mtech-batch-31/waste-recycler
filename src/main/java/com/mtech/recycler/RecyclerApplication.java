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
//            user.setPassword(Utilities.encodePassword("your-password"));
//            user.setRole(Role.CUSTOMER);
//            repo.save(user);
//        };
//    }
}
