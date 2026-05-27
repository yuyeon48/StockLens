package com.example.Stock.repository;

import com.example.Stock.domain.WatchList;
import com.example.Stock.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WatchListRepository extends JpaRepository<WatchList, Long> {
    List<WatchList> findByUser(User user);
    Optional<WatchList> findByUserAndSymbol(User user, String symbol);
    void deleteByUserAndSymbol(User user, String symbol);
}