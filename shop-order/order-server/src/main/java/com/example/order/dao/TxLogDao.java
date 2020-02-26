package com.example.order.dao;

import com.example.order.domain.Order;
import com.example.order.domain.TxLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TxLogDao extends JpaRepository<TxLog, String> {
}
