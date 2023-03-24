package com.tiCloudServer.systemContact;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableAsync
@EnableDiscoveryClient
@ServletComponentScan
@EnableFeignClients
@MapperScan(basePackages = {"com.tiCloudServer.systemContact.mappers"})
public class SystemContactApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemContactApplication.class, args);
    }
}
