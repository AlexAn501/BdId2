package ru.antonov.bdid2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BdId2Application {

    public static void main(String[] args) {
        SpringApplication.run(BdId2Application.class, args);
    }

}
