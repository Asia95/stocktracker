package com.stocktracker.repository;

import com.stocktracker.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Stock findByTicker(String ticker);
}