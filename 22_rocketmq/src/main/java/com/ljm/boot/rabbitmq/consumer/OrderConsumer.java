package com.ljm.boot.rabbitmq.consumer;

import com.ljm.boot.rabbitmq.model.Orders;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @author Dominick Li
 * @CreateTime 2020/3/22 21:06
 * @description 如果consumerGroup的名称不同, 则会产生重复消费的情况，例如 consumer1和consumer2
 * 如果一个top下面有多个consumerGroup组,则消费者 消费消息会根据组的长度取模
 **/
@Service
@RocketMQMessageListener(topic = "${topic.order}", consumerGroup = "${spring.application.name}-${topic.order}-consumer1")
public class OrderConsumer implements RocketMQListener<Orders> {
    @Override
    public void onMessage(Orders orders) {
        System.out.println("OrderConsumer1开始消费" + orders.getName() + "," + orders.getOrderId());
    }
}
