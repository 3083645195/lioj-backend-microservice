package com.zhaoli.liojbackendjudgeservice;

import com.zhaoli.liojbackendjudgeservice.rabbitmq.InItRabbitMQ;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
//表示告诉 Spring 去扫描 com.zhaoli 包及其子包，将其中标记为组件的类注册为 Spring Bean
@ComponentScan("com.zhaoli")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.zhaoli.liojbackendserviceclient.service"})
public class LiojBackendJudgeServiceApplication {
    public static void main(String[] args) {
        //初始化消息队列
        InItRabbitMQ.doInit();
        SpringApplication.run(LiojBackendJudgeServiceApplication.class, args);
    }

}
