package com.example.order;

import cn.hutool.core.util.StrUtil;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.UUID;

/**
 * @description: 测试MQ发送消息
 * @author: lij
 * @create: 2020-02-23 18:45
 */
public class RocketMQSendMessageTests {
    /**测试生产者发送消息*/
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        //1.创建消息生产者，设置生产者组名
        DefaultMQProducer producer = new DefaultMQProducer("test-producer-group");
        //2.为生产者设置NameServer的地址
        producer.setNamesrvAddr("192.168.3.200:9876");
        //3.启动生产者
        producer.start();
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                //4.构建消息对象，主要是设置消息的主题、标签、内容
                String messageContent = StrUtil.format("Test RocketMQ Message: {}", UUID.randomUUID().toString());
                Message message = new Message("test-topic", "test-tag", messageContent.getBytes());
                //5.发送消息，设置超时时间
                SendResult result = producer.send(message, 10000);
                System.out.println(result);

            }
            Thread.sleep(1000);
        }

        //6.关闭生产者
        producer.shutdown();
    }
}
