package com.example.musicStore.controller;

import com.example.musicStore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/orders")
@PreAuthorize("hasRole('ADMIN')")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/metrics/orders-by-user")
    public ResponseEntity<List<Map<String, Object>>> getOrdersCountByUser() {
        List<Map<String, Object>> metrics = orderService.getOrdersCountByUser();
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/metrics/total-price-by-user")
    public ResponseEntity<List<Map<String, Object>>> getTotalPriceByUser() {
        List<Map<String, Object>> metrics = orderService.getTotalPriceByUser();
        return ResponseEntity.ok(metrics);
    }
}