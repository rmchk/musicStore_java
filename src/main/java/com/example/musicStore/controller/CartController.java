package com.example.musicStore.controller;

import com.example.musicStore.model.Cart;
import com.example.musicStore.model.Order;
import com.example.musicStore.service.CartService;
import com.example.musicStore.service.OrderService;
import com.example.musicStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    // Получить корзину текущего пользователя
    @GetMapping
    public Cart getCart(Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        return cartService.getCartByUser(userId);
    }

    // Добавить товар в корзину
    @PostMapping("/add/{productId}")
    public ResponseEntity<?> addToCart(@PathVariable Long productId, Authentication authentication) {
        try {
            Long userId = getUserIdFromAuthentication(authentication);
            Cart cart = cartService.addProductToCart(userId, productId);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding product to cart: " + e.getMessage());
        }
    }

    // Удалить товар из корзины
    @DeleteMapping("/remove/{productId}")
    public Cart removeFromCart(@PathVariable Long productId, Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        return cartService.removeProductFromCart(userId, productId);
    }

    // Оформить заказ
    @PostMapping("/checkout")
    public Order checkout(Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        return orderService.createOrder(userId);
    }

    // Получить историю заказов
    @GetMapping("/orders")
    public List<Order> getOrders(Authentication authentication) {
        System.out.println("GET /api/cart/orders - Authentication: " + authentication);
        Long userId = getUserIdFromAuthentication(authentication);
        System.out.println("GET /api/cart/orders - User ID: " + userId);
        List<Order> orders = orderService.getOrdersByUser(userId);
        System.out.println("GET /api/cart/orders - Returning orders: " + orders);
        return orders;
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        String username = authentication.getName();
        return userService.getUserIdByUsername(username);
    }
}