# lioj-backend-microservice

将之前的zhaoli-oj-backend单体项目改造为**微服务**

### 什么是微服务?

服务:提供某类功能的代码<br>
微服务:
专注于提供某类特定功能的代码，而不是把所有的代码全部放到同一个项目里。会把整个大的项目按照定的功能、逻辑进行拆分，拆分为多个子模块，每个子块可以独立运行、独立负责一类功能，子块之间相互调用、互不影响。<br>

一个公司:一个人干活，这个人icu
了，公司直接倒闭一个公司有多个不同类的岗位，多个人干活，一个组跨了还有其他组可以正常工作，不会说公司直接倒闭。各组之间可能需要交互，来完成大的目标。<br>
微服务的几个重要的实现因素:**服务管理、服务调用、服务拆分**<br>

### 微服务实现技术?

Spring Cloud<br>
**Spring Cloud Alibaba(本项目采用)**<br>
Dubbo (DubboX)<br>
RPC(GRPC、TRPC)<br>
**本质上是通过 HTTP、或者其他的网络协议进行通讯来实现的**。<br>

### Spring Cloud Alibaba

https://github.com/alibaba/spring-cloud-alibaba <br>
推荐参考中文文档来学习:https://sca.aliyun.com/zh-cn/ <br>
本质:是在 Spring Cloud 的基础上，进行了增强，补充了一些额外的能力，根据阿里多年的业务沉淀做了一些定制化的开发<br>

1. Spring Cloud Gateway:网关
2. Nacos:服务注册和配置中心
3. Sentinel:熔断限流
4. Seata:分布式事务
5. RocketMQ:消息队列，削峰填谷
6. Docker:使用Docker进行容器化部署
7. Kubernetes:使用k8s进行容器化部署

![img_1.png](doc%2Fimg_1.png)

注意，一定要选择对应的版本:https://sca.aliyun.com/zh-cn/docs/2021.0.5.0/overview/version-explain <br>
本项目选择 2021.0.5.0 <br>
Nacos:集中存管项目中所有服务的信息，便于服务之间找到彼此;同时，还支持集中存储整个项目中的配置<br>
整个微服务请求流程:
![img_2.png](doc%2Fimg_2.png)

## 改造前思考

从业务需求出发，思考单机和分布式的区别
用户登录功能:需要改造为分布式登录
其他内容:

1. 有没有用到单机的锁? 改造为分布式锁
2. 有没有用到本地缓存?改造为分布式缓存(Redis)
3. 需不需要用到分布式事务?比如操作多个库

### 改造分布式登录

1. application.yml 增加 redis 配置
2. 补充依赖:

```xml
<!-- redis -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
<groupId>org.springframework.session</groupId>
<artifactId>spring-session-data-redis</artifactId>
</dependency>
```

3. 主类取消 Redis 自动配置的移除
4. 修改 session 存储方式:

```yaml
spring.session.store-type:redis
```

5.使用 redis-cli 或者 redis 管理工具，查看是否有登录后的信息

### 微服务的划分

从业务出发，想一下哪些功能/职责是一起的?
> 公司老板给员工分工

1. 依赖服务:

- 注册中心:Nacos
- 微服务网关(lioj-backend-gateway)Gateway 聚合所有的接口，统一接受处理前端的请求

2. 公共模块:

- common 公共模块(lioj-backend-common):全同异常处理器、请求响应封装类、公用的工具类等
- model 模型模块(lioj-backend-model):很多服务公用的实体类
- 公用接口模块(lioj-backend-service-client):只存放接口，不存放实现(多个服务之间要共享)

3. 业务功能:

- 用户服务(lioj-backend-user-service:8081 端口)
    - 注册
    - 登录
    - 用户管理
- 题目服务(lioj-backend-question-service:8082)
    - 创建题目(管理员)
    - 删除题目(管理员)
    - 修改题目(管理员)
    - 搜索题目(用户)
    - 在线做题
    - **题目提交**
- 判题服务(lioj-backend-judge-service，8103 端口，较重的操作)
    - 执行判题逻辑
    - 错误处理(内存溢出、安全性、超时)
    - **自主实现** 代码沙箱(安全沙箱)
    - 开放接口(提供一个独立的新服务)

> 代码沙箱服务本身就是独立的，不用纳入Spring Cloud 的管理

### 路由划分

用 springboot 的 context-path 统一修改各项目的接口前缀，比如:

1. 用户服务:

- /api/user
- /api/user/inner(内部调用，网关层面要做限制)

2. 题目服务:

- /api/question(也包括题目提交信息)
- /api/question/inner(内部调用，网关层面要做限制)

3. 判题服务:

- /api/judge
- /api/judge/inner(内部调用，网关层面要做限制)

### Nacos 注册中心启动

**选择 2.2.0 版本**<br>
教程:https://sca.aliyun.com/zh-cn/docs/2021.0.5.0/user-guide/nacos/overviewNacos <br>
官网教程:https://nacos.io/zh-cn/docs/quick-start.html <br>
到官网下载 Nacos: https://github.com/alibaba/nacos/releases/tag/2.2.0 <br>
安装好后，进入 bin 目录启动:

```Shell
startup.cmd -m standalone
```

### 新建工程

本项目用脚手架创建项目:https://start.aliyun.com/ <br>
给项目增加全局依赖配置文件。<br>
创建完初始项目后，补充 Spring Cloud 依赖:

```xml

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-dependencies</artifactId>
    <version>2021.0.5</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```

依次使用 new modules 和 spring boot Initializr 创建各模块<br>
![img_3.png](doc%2Fimg_3.png) <br>
需要给各模块之间绑定子父依赖关系<br>
![img_4.png](doc%2Fimg_4.png) <br>
父模块定义 modules，子模块引入 parent 语法，可以通过继承父模块配置，统一项目的定义和版本号。

### 同步代码和依赖

1. common 公共模块(yuoj-backend-common):全局异常处理器、请求响应封装类、公用的工具类等
2. model模型模块(yuoj-backend-model):很多服务公用的实体类直接复制 model 包，注意代码沙箱 model的引入
3. 公用接口模块(yuoj-backend-service-client):只存放接口，不存放实现(多个服务之间要共享先无脑搬运所有的
   service，judgeService 也需要搬运

- 需要指定 openfeign(客户端调用工具)的版本

4. 具体业务服务实现

- 给所有业务服务引入公共依赖
- 主类引入注解
- 引入 application.yml 配置

### 服务内部调用

现在的问题是，题目服务依赖用户服务，但是代码已经分到不同的包，找不到对应的 Bean。<br>
可以使用 Open Feign 组件实现跨服务的远程调用。<br>
Open Feign:Http 调用客户端，提供了更方便的方式来让你远程调用其他服务，不用关心服务的调用地址<br>
Nacos 注册中心获取服务调用地址<br>

**1. 梳理服务的调用关系，确定哪些服务(接口)需要给内部调用**

1. 用户服务:没有其他的依赖
2. 题目服务:

- userService.getById(userld)
- userService.getUserVO(user)
- userService.listByIds(userldSet)
- userService.isAdmin(loginUser)
- userService.getLoginUser(request)
- judgeService.doJudge(questionSubmitId)

3. 判题服务:

- questionService.getById(questionId)
- questionSubmitService.getById(questionSubmitId)
- questionSubmitService.updateById(questionSubmitUpdate)

**2. 确认要提供哪些服务**

1. 用户服务:

- userService.getById(userld)
- userService.getUserVO(user)
- userService.listByIds(userldSet)
- userService.isAdmin(loginUser)
- userService.getLoginUser(request)

2. 题目服务:

- questionService.getById(questionId)
- questionSubmitService.getById(questionSubmitId)
- questionSubmitService.updateById(questionSubmitUpdate)

3. 判题服务:

- judgeService.doJudge(questionSubmitId)

**3. 实现 client 接口**<br>
对于用户服务，有一些不利于远程调用参数传递、 或者实现起来非常简单(工具类)可以直接用默认方法，无需远程调用，节约性能 <br>
开启 openfeign 的支持，把我们的接口暴露出去(服务注册到注册中心上)，作为 API 给其他服务调用(其他服务从注册中心寻找) <br>
需要修改每个服务提供者的 context-path 全局请求路径 <br>
**服务提供者**:理解为接口的实现类，实际提供服务的模块(服务注册到注册中心上) <br>
**服务消费者**:理解为接口的调用方，需要去找到服务提供者，然后调用。(其他服务从注册中心寻找) <br>
**注意事项**:

- 要给接口的每个方法打上请求注解，注意区分 Get、Post
- 要给请求参数打上注解，比如 RequestParam、RequestBody
- FeignClient 定义的请求路径一定要和服务提供方实际的请求路径保持一致

**4. 修改各业务服务的调用代码为 feignClient**

**5. 编写 feignClient 服务的实现类，注意要和之前定义的客户端保持一致**

**6. 开启 Nacos 的配置，让服务之间能够互相发现**
所有模块引入 Nacos 依赖，然后给业务服务(包括网关)增加配置

```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
```

给业务服务项目启动类打上注解，开启服务发现、找到对应的客户端 Bean 的位置

```
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.zhaoli.liojbackendserviceclient.service"})
```

全局引入负载均衡器依赖

```xml

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-loadbalancer</artifactId>
    <version>3.1.5</version>
</dependency>
```

**7. 启动项目，测试依赖能否注入，能否完成相互调用**

### 微服务网关

微服务网关(lioj-backend-gateway):Gateway 聚合所有的接口，统一接受处理前端的请求<br>
为什么要用?

- 所有的服务端口不同，增大了前端调用成本
- 所有服务是分散的，你可需要集中进行管理、操作，比如集中解决跨域、鉴权、接口文档、服务的路由、接口安全性、流量染色、限流

> Gateway:想自定义一些功能，需要对这个技术有比较深的理解

Gateway 是应用层网关:会有一定的业务逻辑(比如根据用户信息判断权限)<br>
Nginx 是接入层网关:比如每个请求的日志，通常没有业务逻辑<br>
**1. 接口路由**
统一地接受前端的请求，转发请求到对应的服务<br>
如何找到路由?可以编写一套路由配置，通过 api地址前缀来找到对应的服务<br>
```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848 #nacos中心地址
    gateway:
      routes:
        - id: lioj-backend-user-service
          uri: lb://lioj-backend-user-service
          predicates:
            - Path=/api/user/**
        - id: lioj-backend-question-service
          uri: lb://lioj-backend-question-service
          predicates:
            - Path=/api/question/**
        - id: lioj-lioj-backend-judge-service
          uri: lb://lioj-backend-judge-service
          predicates:
            - Path=/api/judge/**
  application:
    name: lioj-backend-gateway
  # 指定应用程序的 Web 类型为响应式（reactive）
  main:
    web-application-type: reactive
server:
  port: 8101
```
