package com.ljm.boot.rabbitmq.listen;

import com.ljm.boot.rabbitmq.model.Orders;
import com.ljm.boot.rabbitmq.service.OrdersService;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import java.util.Optional;


@RocketMQTransactionListener
public class TransactionListener implements RocketMQLocalTransactionListener {

    @Autowired
    OrdersService ordersService;

    /**
     * 执行本地事务
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        String transId = (String) msg.getHeaders().get(RocketMQHeaders.TRANSACTION_ID);
        System.out.printf("#### executeLocalTransaction  transactionId=%s %n",
                transId);
        //执行本地事务，并记录日志
        ordersService.save((Orders) arg);
        //执行成功，可以提交事务
        return RocketMQLocalTransactionState.COMMIT;

    }

    /**
     * 检查本地事务是否成功
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        String transId = (String) msg.getHeaders().get(RocketMQHeaders.TRANSACTION_ID);
        System.out.printf("#### checkLocalTransaction transactionId=%s %n", transId);
        Optional<Orders> ordersOptional = ordersService.findById(Long.parseLong(transId));
        if (ordersOptional.isPresent()) {
            return RocketMQLocalTransactionState.COMMIT;
        } else {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

}
