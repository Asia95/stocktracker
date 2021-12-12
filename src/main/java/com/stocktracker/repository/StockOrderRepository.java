package com.stocktracker.repository;

import com.stocktracker.model.StockOrder;
import com.stocktracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface StockOrderRepository extends JpaRepository<StockOrder, Long> {
    List<StockOrder> findAllByUser(User user);

    List<StockOrder> findAllByUserAndBoughtAt(User user, Date date);
}