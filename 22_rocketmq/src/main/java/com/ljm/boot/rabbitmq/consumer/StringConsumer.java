package com.ljm.boot.rabbitmq.consumer;

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
@RocketMQMessageListener(topic = "${topic.string}", consumerGroup = "${spring.application.name}-${topic.string}-consumer1")
public class StringConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String msg) {
        System.out.println("StringConsumer1开始消费:"+ msg);
//        if(true){
//            //模拟出现异常,出现异常后这个任务还会被重试消费
//            System.out.println(1/0);
//        }
    }
}
