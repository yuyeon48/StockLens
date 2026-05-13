package com.example.Stock.service;

import com.example.Stock.domain.Stock;
import com.example.Stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiRecommendService {

    private final RestTemplate restTemplate;
    private final StockRepository stockRepository;

    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";

    public String recommend() {
        List<Stock> stocks = stockRepository.findAll();

        StringBuilder sb = new StringBuilder();
        sb.append("현재 한국 시가총액 Top50 주식 데이터입니다:\n\n");
        for (int i = 0; i < stocks.size(); i++) {
            Stock s = stocks.get(i);
            sb.append(String.format("%d. %s(%s) - 현재가: %,.0f원, 등락률: %+.2f%%\n",
                    i + 1, s.getName(), s.getSymbol(), s.getPrice(), s.getChangePercent()));
        }
        sb.append("\n위 데이터를 바탕으로 투자 유망 종목 3개를 추천하고 이유를 간단히 설명해주세요.");

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gemma3:1b");
        body.put("prompt", sb.toString());
        body.put("stream", false);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(OLLAMA_URL, request, Map.class);

        return (String) response.getBody().get("response");
    }
}