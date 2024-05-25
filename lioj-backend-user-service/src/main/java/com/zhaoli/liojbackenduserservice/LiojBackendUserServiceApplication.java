package com.zhaoli.liojbackenduserservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

// todo 如需开启 Redis，须移除 exclude 中的内容
//@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
@SpringBootApplication
@MapperScan("com.zhaoli.liojbackenduserservice.mapper")
@EnableScheduling
/**
 * 用于启用AspectJ自动代理功能。通过这个注解，Spring将会自动为标记了@Aspect注解的切面类创建代理，以便在目标对象的方法执行前后插入切面逻辑
 * proxyTargetClass = true表示使用CGLIB代理，即基于类的代理。这意味着Spring将会为目标对象创建一个子类来实现代理。
 * exposeProxy = true表示将代理对象暴露给切面类，以便在切面内部通过AopContext.currentProxy()方法获取代理对象。
 */
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
//表示告诉 Spring 去扫描 com.zhaoli 包及其子包，将其中标记为组件的类注册为 Spring Bean
@ComponentScan("com.zhaoli")
//启用服务发现功能
@EnableDiscoveryClient
/**
 * 用于启用Feign客户端功能。Feign是一个声明式的HTTP客户端，可以简化HTTP API的调用，使得服务之间的通信更加简单和直观。
 * 通过@EnableFeignClients注解，可以扫描指定的包路径，自动创建Feign客户端的实例，以便在应用程序中进行远程服务的调用。
 */
@EnableFeignClients(basePackages = {"com.zhaoli.liojbackendserviceclient.service"})
public class LiojBackendUserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(LiojBackendUserServiceApplication.class, args);
    }

}
