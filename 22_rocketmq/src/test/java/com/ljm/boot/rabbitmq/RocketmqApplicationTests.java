package com.ljm.boot.rabbitmq;

import com.ljm.boot.rabbitmq.model.Orders;
import com.ljm.boot.rabbitmq.repository.OrdersRepository;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootTest
class RocketmqApplicationTests {


    @Resource
    private RocketMQTemplate rocketMQTemplate;
    @Value("${topic.string}")
    private String stringTopic;
    @Value("${topic.order}")
    private String orderTopic;

    @Autowired
    OrdersRepository ordersRepository;

    @Test
    void contextLoads() {
        //testBaseMethod();
        testTransactionMethod();
    }

    /**
     * send  默认同步发送,底层调用的syncSend方法
     * syncSend 同步发送消息，能接收发送结果
     * asyncSend 异步发送消息,回调处理结果
     */
    public void testBaseMethod() {
        //1.发送异步消息,但是不会确认消息有没有被接收,日志可以这么发,如果是对数据一致性要去比较高的,建议使用下面的方法
        rocketMQTemplate.send(stringTopic, MessageBuilder.withPayload(String.format("test send method")).build());
        //2. 发送同步消息,等待发送消息返回的结果
        SendResult sendResult = rocketMQTemplate.syncSend(stringTopic, "test syncSend method");
        if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
            System.out.println("syncSend 发送成功...");
        }
        //3.发送异步消息,回调里处理发送成功或失败逻辑
        for (int j = 1; j < 3; j++) {
            rocketMQTemplate.asyncSend(stringTopic, "test asyncSend method", new SendCallback() {
                @Override
                public void onSuccess(SendResult sr) {
                    if (sr.getSendStatus() == SendStatus.SEND_OK) {
                        System.out.println("async onSucess  ok");
                    } else {
                        System.out.println("async onSucess  fail");
                    }
                }
                @Override
                public void onException(Throwable var1) {
                    System.out.printf("async onException Throwable=%s %n", var1);
                }

            });
            System.out.printf("发送第%d条消息\n", j);
        }
    }

    /**
     * 发送事务消息
     */
    public void testTransactionMethod() {
        Long orderId = System.currentTimeMillis();
        Orders order = new Orders();
        order.setOrderId(orderId);
        order.setName("酒水订单");
        order.setCreateDate(new Date());
        rocketMQTemplate.sendMessageInTransaction(orderTopic,
                MessageBuilder.withPayload(
                        order)
                        .setHeader(RocketMQHeaders.TRANSACTION_ID, orderId)
                        .build()
                , order);
    }


}
