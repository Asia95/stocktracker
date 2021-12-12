package com.stocktracker.repository;

import com.stocktracker.model.Portfolio;
import com.stocktracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    Portfolio findByUser(User user);
}
