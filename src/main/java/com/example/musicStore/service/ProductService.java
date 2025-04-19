package com.example.musicStore.service;

import com.example.musicStore.model.Product;
import com.example.musicStore.repository.CartRepository;
import com.example.musicStore.repository.OrderRepository;
import com.example.musicStore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    // Получение всех товаров
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Получение товара по ID
    public Product getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }

    // Получение списка уникальных категорий
    public Set<String> getAllCategories() {
        return productRepository.findAll()
                .stream()
                .map(Product::getCategory)
                .filter(category -> category != null && !category.isEmpty())
                .collect(Collectors.toSet());
    }

    // Получение списка уникальных брендов
    public Set<String> getAllBrands() {
        return productRepository.findAll()
                .stream()
                .map(Product::getBrand)
                .filter(brand -> brand != null && !brand.isEmpty())
                .collect(Collectors.toSet());
    }

    // Сохранение товара (добавление или обновление)
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // Удаление товара по ID
    public void deleteProduct(Long id) {
        // Удаляем товар из всех корзин
        cartRepository.findAll().forEach(cart -> {
            cart.getProducts().removeIf(product -> product.getId().equals(id));
            cartRepository.save(cart);
        });

        // Удаляем товар из всех заказов
        orderRepository.findAll().forEach(order -> {
            order.getProducts().removeIf(product -> product.getId().equals(id));
            orderRepository.save(order);
        });

        // Удаляем сам товар
        productRepository.deleteById(id);
    }
}