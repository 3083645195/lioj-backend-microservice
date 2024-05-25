package com.zhaoli.liojbackendquestionservice;

import com.zhaoli.liojbackendquestionservice.rabbitmq.MyMessageProducer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class MyMessageProducerTest {
    @Resource
    private MyMessageProducer myMessageProducer;

    @Test
    void sendMessage() {
        myMessageProducer.sendMessage("zhaoli_oj", "my_routingKey", "你好呀");
    }
}