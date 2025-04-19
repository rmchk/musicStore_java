package com.example.musicStore.service;
import com.example.musicStore.model.User;
import com.example.musicStore.model.Cart;
import com.example.musicStore.model.Order;
import com.example.musicStore.model.Product;
import com.example.musicStore.model.CartItem;
import com.example.musicStore.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Transactional
    public Order createOrder(Long userId) {
        // Находим пользователя
        User user = userService.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("Пользователь с ID " + userId + " не найден");
        }

        // Находим корзину пользователя
        Cart cart = cartService.getCartByUser(userId);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new IllegalStateException("Корзина пуста");
        }

        // Создаём новый заказ
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());

        // Копируем товары из корзины в новый список
        List<Product> products = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            int quantity = item.getQuantity();
            // Добавляем товар в заказ столько раз, сколько указано в quantity
            for (int i = 0; i < quantity; i++) {
                products.add(product);
            }
        }
        order.setProducts(products);

        // Вычисляем общую стоимость
        double totalPrice = cart.getItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
        order.setTotalPrice(totalPrice);

        // Сохраняем заказ
        order = orderRepository.save(order);

        // Очищаем корзину после создания заказа
        cartService.clearCart(userId);

        return order;
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}