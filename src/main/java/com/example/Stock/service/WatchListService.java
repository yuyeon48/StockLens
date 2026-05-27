package com.example.Stock.service;

import com.example.Stock.domain.User;
import com.example.Stock.domain.WatchList;
import com.example.Stock.repository.UserRepository;
import com.example.Stock.repository.WatchListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WatchListService {

    private final WatchListRepository watchListRepository;
    private final UserRepository userRepository;

    // 관심종목 추가
    public void addWatchList(String email, String symbol, String name) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        // 이미 추가된 종목이면 스킵
        if (watchListRepository.findByUserAndSymbol(user, symbol).isPresent()) return;

        WatchList watchList = new WatchList();
        watchList.setUser(user);
        watchList.setSymbol(symbol);
        watchList.setName(name);
        watchListRepository.save(watchList);
    }

    // 관심종목 삭제
    @Transactional
    public void removeWatchList(String email, String symbol) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        watchListRepository.deleteByUserAndSymbol(user, symbol);
    }

    // 관심종목 조회
    public List<WatchList> getWatchList(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        return watchListRepository.findByUser(user);
    }

    // 관심종목 여부 확인
    public boolean isWatched(String email, String symbol) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        return watchListRepository.findByUserAndSymbol(user, symbol).isPresent();
    }
}