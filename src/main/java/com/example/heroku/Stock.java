package com.example.heroku;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@ToString
@NoArgsConstructor
@Setter
@Getter
public class Stock implements Serializable {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private Double price;
    private Integer amount;
    private String ticker;

}
