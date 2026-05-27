package com.example.Stock.controller;

import com.example.Stock.domain.Stock;
import com.example.Stock.service.AiRecommendService;
import com.example.Stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.Stock.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.util.List;
import com.example.Stock.service.WatchListService;

@Controller
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;
    private final AiRecommendService aiRecommendService;
    private final UserRepository userRepository;
    private final WatchListService watchListService;

    // 메인 화면
    @GetMapping
    public String index(Model model, @AuthenticationPrincipal Object principal) {
        model.addAttribute("stocks", stockService.getAllStocks());

        if (principal instanceof org.springframework.security.core.userdetails.User userDetails) {
            userRepository.findByEmail(userDetails.getUsername())
                    .ifPresent(user -> model.addAttribute("username", user.getName()));
        } else if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User oauth2User) {
            model.addAttribute("username", oauth2User.getAttribute("name"));
        }

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

    // 관심종목 추가
    @PostMapping("/watchlist/add")
    @ResponseBody
    public ResponseEntity<String> addWatchList(@RequestParam String symbol,
                                               @RequestParam String name,
                                               @AuthenticationPrincipal Object principal) {
        String email = getEmail(principal);
        if (email == null) return ResponseEntity.status(401).body("로그인이 필요합니다.");
        watchListService.addWatchList(email, symbol, name);
        return ResponseEntity.ok("추가됨");
    }

    // 관심종목 삭제
    @PostMapping("/watchlist/remove")
    @ResponseBody
    public ResponseEntity<String> removeWatchList(@RequestParam String symbol,
                                                  @AuthenticationPrincipal Object principal) {
        String email = getEmail(principal);
        if (email == null) return ResponseEntity.status(401).body("로그인이 필요합니다.");
        watchListService.removeWatchList(email, symbol);
        return ResponseEntity.ok("삭제됨");
    }

    // 관심종목 목록 조회
    @GetMapping("/watchlist")
    @ResponseBody
    public ResponseEntity<?> getWatchList(@AuthenticationPrincipal Object principal) {
        String email = getEmail(principal);
        if (email == null) return ResponseEntity.status(401).body("로그인이 필요합니다.");
        return ResponseEntity.ok(watchListService.getWatchList(email));
    }

    // 이메일 추출 헬퍼
    private String getEmail(Object principal) {
        if (principal instanceof org.springframework.security.core.userdetails.User u) {
            return u.getUsername();
        } else if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User o) {
            return o.getAttribute("email");
        }
        return null;
    }

}