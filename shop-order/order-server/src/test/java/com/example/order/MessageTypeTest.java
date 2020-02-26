package com.example.order;

import com.example.order.dao.OrderDao;
import com.example.order.dao.TxLogDao;
import com.example.order.domain.Order;
import com.example.order.domain.TxLog;
import com.example.order.service.OrderService;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
 * @description: 测试消息类型
 * @author: lij
 * @create: 2020-02-23 23:07
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderServerApplication.class)
public class MessageTypeTest {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private OrderDao orderDao;

    //测试同步消息
    @Test
    public void testSyncSend(){
        //参数1 topic:tag 参数2 消息体 参数3 超时时间（单位ms）
        SendResult result = rocketMQTemplate.syncSend("test-sync", "这是一条消息", 10000);
        System.out.println(result);
        SendResult result2 = rocketMQTemplate.syncSend("test-sync:myTag", "这是一条消息", 10000);
        System.out.println(result2);
    }

    //测试异步消息
    @Test
    public void testAsyncSend() throws InterruptedException {
        //参数1 topic:tag 参数2 消息体 参数3 回调
        rocketMQTemplate.asyncSend("test-async", "这是一条消息", new SendCallback() {
            //成功回调
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println(sendResult);
            }
            //异常回调
            @Override
            public void onException(Throwable throwable) {
                System.out.println(throwable);
            }
        });
        Thread.sleep(1000000L); //为了让回调时线程没有结束，阻塞住线程，
    }

    //测试单向消息
    @Test
    public void testOneWaySend(){
        //参数1 topic:tag 参数2 消息体 参数3 超时时间（单位ms）
        rocketMQTemplate.sendOneWay("test-oneWay", "这是一条消息");
    }

    //测试顺序消息
    @Test
    public void testOrderedSend() throws InterruptedException {
        //为了能看出只往一个message queue里发送，每种发100条
        for (int i = 0; i < 100; i++) {
            //同步顺序消息
            //参数1 topic:tag 参数2 消息体
            //参数3 一个字符串值，用于对他取hash，决定消息发送到哪个队列上
            SendResult result = rocketMQTemplate.syncSendOrderly("test-sync", "这是顺序消息", "123");

            //异步顺序消息
            //参数3 一个字符串值，用于对他取hash，决定消息发送到哪个队列上
            rocketMQTemplate.asyncSendOrderly("test-async", "这是一条消息", "123",  new SendCallback() {
                //成功回调
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println(sendResult);
                }
                //异常回调
                @Override
                public void onException(Throwable throwable) {
                    System.out.println(throwable);
                }
            });

            //单向顺序消息
            rocketMQTemplate.sendOneWayOrderly("test-oneWay", "这是顺序消息", "123");
        }

        Thread.sleep(10000000L);
    }

    //测试事务消息
    @Test
    public void createOrderBefore(){
        Order order = new Order();
        order.setUsername("xxxx").setNumber(10).setUid(1L).setProductName("xxxx").setPrice(3000.0).setPid(11L);
        //准备一个txId唯一标识这次事务
        String txId = UUID.randomUUID().toString();
        //发送半事务消息
        //参数1 生产者组名
        //参数2 主题topic
        //参数3 消息体
        //参数4 参数，会被传递给执行本地事务和消息回查的方法
        TransactionSendResult result = rocketMQTemplate.sendMessageInTransaction(
                "test-producer-group",
                "tx-topic",
                MessageBuilder.withPayload(order).setHeader("txId", txId).build(), //可以通过header来传递其他参数
                order);
    }

}
