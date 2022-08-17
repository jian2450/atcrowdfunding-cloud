package com.atguigu.atcrowdfunding.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class AtcrowdfundingCloudManagerApplication {

	/**
	 * 
	 * @date 2022年5月7日下午5:21:15
	 * RestTemplate默认无法查找注册中心的服务，需要增加负载均衡注解
	 */
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	
	public static void main(String[] args) {
		SpringApplication.run(AtcrowdfundingCloudManagerApplication.class, args);
	}

}
