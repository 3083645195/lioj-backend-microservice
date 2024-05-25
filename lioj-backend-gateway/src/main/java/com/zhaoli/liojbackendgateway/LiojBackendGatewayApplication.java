package com.zhaoli.liojbackendgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 使用exclude = {DataSourceAutoConfiguration.class}将会排除DataSourceAutoConfiguration类的自动配置
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
public class LiojBackendGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(LiojBackendGatewayApplication.class, args);
    }

}
