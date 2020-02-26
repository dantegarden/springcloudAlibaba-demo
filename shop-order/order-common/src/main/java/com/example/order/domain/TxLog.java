package com.example.order.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @description: 消息事务状态
 * @author: lij
 * @create: 2020-02-24 00:18
 */
@Entity(name = "shop_txlog")
@Data
@Accessors(chain = true)
public class TxLog {
    @Id
    private String txId;
    private Date date;
}
