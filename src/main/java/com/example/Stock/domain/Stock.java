package com.example.Stock.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;        // 종목코드 (예: 005930)
    private String name;          // 종목명
    private Double price;         // 현재가
    private Double change;        // 전일대비
    private Double changePercent; // 등락률
    private Long volume;          // 거래량
}