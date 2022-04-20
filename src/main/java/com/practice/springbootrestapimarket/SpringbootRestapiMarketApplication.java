package com.practice.springbootrestapimarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringbootRestapiMarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootRestapiMarketApplication.class, args);
    }

}
