package com.stocktracker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.util.Date;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockOrder {
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long orderId;
    @NotBlank(message = "Amount is required")
    private Float amount;
    @NotBlank(message = "Price is required")
    private Float price;
    @NotBlank(message = "Date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date boughtAt;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "stockId", referencedColumnName = "stockId")
    private Stock stock;
}
