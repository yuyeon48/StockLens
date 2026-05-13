package com.example.Stock;

import com.example.Stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class StockApplication {

    private final StockService stockService;

    public static void main(String[] args) {
        SpringApplication.run(StockApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {
            System.out.println("서버 시작 - Top50 데이터 로딩 중...");
            stockService.getAllStockPrices();
            System.out.println("데이터 로딩 완료!");
        };
    }
}