package com.example.musicStore.repository;

import com.example.musicStore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}