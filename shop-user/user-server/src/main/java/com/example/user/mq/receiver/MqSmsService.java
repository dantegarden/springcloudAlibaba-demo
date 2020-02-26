package com.example.user.mq.receiver;

import com.example.order.domain.Order;
import com.example.user.utils.SmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: lij
 * @create: 2020-02-23 22:51
 */
@Service
@RocketMQMessageListener(
        consumerGroup = "shop-user",    //指定消费者组名
        topic = "order-topic",          //消费的主题
        consumeMode = ConsumeMode.CONCURRENTLY, //消费模式，CONCURRENTLY同步消费(默认), ORDERLY 顺序消费
        messageModel = MessageModel.CLUSTERING)  //消息模式， 集群（其实就是发布订阅），广播
@Slf4j
public class MqSmsService implements RocketMQListener<Order> {
    //消费的逻辑
    @Override
    public void onMessage(Order order) {
        log.info("从MQ接收到订单消息{}， 准备发短信", order);
        //发送短信
        //SmsUtil.sendSms(...);
    }
}
