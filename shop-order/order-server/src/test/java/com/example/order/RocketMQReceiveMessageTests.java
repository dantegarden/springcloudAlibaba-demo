package com.example.order;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @description: 测试MQ订阅消息
 * @author: lij
 * @create: 2020-02-23 22:13
 */
public class RocketMQReceiveMessageTests {
    public static void main(String[] args) throws MQClientException {
        //1创建消费者，并且为它指定消费者组的名称
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test-consumer-group");
        //2为消费者设置NameServer的地址
        consumer.setNamesrvAddr("192.168.3.200:9876");
        //3指定消费者订阅的主题和标签
        consumer.subscribe("test-topic", "*"); //test-topic所有标签
        //4设置一个回调函数，并在函数中编写接收到消息后的处理方法
        consumer.registerMessageListener(new MessageListenerConcurrently(){
            //处理获得的消息
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                //消费的逻辑
                list.forEach(System.out::println);
                //回复消费成功
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //5启动消费者
        consumer.start();
    }
}
