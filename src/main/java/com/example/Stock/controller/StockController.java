package com.example.Stock.controller;

import com.example.Stock.domain.Stock;
import com.example.Stock.service.AiRecommendService;
import com.example.Stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;
    private final AiRecommendService aiRecommendService;

    // 메인 화면
    @GetMapping
    public String index(Model model) {
        model.addAttribute("stocks", stockService.getAllStocks());
        return "index";
    }

    // Top50 새로고침
    @PostMapping("/refresh")
    @ResponseBody
    public ResponseEntity<List<Stock>> refresh() {
        return ResponseEntity.ok(stockService.getAllStockPrices());
    }

    // 단일 종목 조회
    @GetMapping("/api/{symbol}")
    @ResponseBody
    public ResponseEntity<Stock> getStock(@PathVariable String symbol) {
        return ResponseEntity.ok(stockService.getAllStocks()
                .stream()
                .filter(s -> s.getSymbol().equals(symbol))
                .findFirst()
                .orElseThrow());
    }

    // AI 추천
    @GetMapping("/ai-recommend")
    @ResponseBody
    public ResponseEntity<String> aiRecommend() {
        return ResponseEntity.ok(aiRecommendService.recommend());
    }
}