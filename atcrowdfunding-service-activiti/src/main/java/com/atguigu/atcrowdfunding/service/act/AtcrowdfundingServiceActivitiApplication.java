package com.atguigu.atcrowdfunding.service.act;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(exclude=SecurityAutoConfiguration.class)
public class AtcrowdfundingServiceActivitiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtcrowdfundingServiceActivitiApplication.class, args);
    }

}
