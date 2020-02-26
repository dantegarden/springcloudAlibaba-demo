package com.example.order.config;

import com.example.order.dao.OrderDao;
import com.example.order.dao.TxLogDao;
import com.example.order.domain.Order;
import com.example.order.domain.TxLog;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @description: 测试事务消息用的configuration
 * @author: lij
 * @create: 2020-02-24 00:43
 */
@Configuration
public class TestTxMsgConfig {

    @Service
    public class TxOrderService {

        @Autowired
        private TxLogDao txLogDao;
        @Autowired
        private OrderDao orderDao;

        @Transactional
        public void createOrder(String txId, Order order){
            //创建订单
            orderDao.save(order);
            //记录事务
            TxLog txLog = new TxLog().setTxId(txId).setDate(new Date());
            txLogDao.save(txLog);
        }
    }

    @Service
    @RocketMQTransactionListener(txProducerGroup = "test-producer-group")
    public class TestTxMessageListener implements RocketMQLocalTransactionListener {
        @Autowired
        private TxOrderService orderService;
        @Autowired
        private TxLogDao txLogDao;

        //执行本地事务
        @Override
        public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {

            String txId = (String) msg.getHeaders().get("txId");

            try {
                Order order = (Order) arg;
                orderService.createOrder(txId, order);
                //事务正常提交
                return RocketMQLocalTransactionState.COMMIT;
            } catch (Exception e) {
                //当异常发生时
                e.printStackTrace();
                return RocketMQLocalTransactionState.ROLLBACK;
            }
        }
        //消息回查
        @Override
        public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
            String txId = (String) msg.getHeaders().get("txId");
            if(txLogDao.findById(txId).isPresent()){
                return RocketMQLocalTransactionState.COMMIT;
            }
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }
}
