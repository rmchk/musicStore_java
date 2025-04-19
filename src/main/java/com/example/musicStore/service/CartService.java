package com.example.musicStore.service;

import com.example.musicStore.model.Cart;
import com.example.musicStore.model.Product;
import com.example.musicStore.model.User;
import com.example.musicStore.repository.CartRepository;
import com.example.musicStore.repository.ProductRepository;
import com.example.musicStore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public Cart getCartByUser(Long userId) {
        System.out.println("Getting cart for userId: " + userId);
        Optional<Cart> cartOpt = cartRepository.findByUserId(userId);
        if (cartOpt.isEmpty()) {
            System.out.println("Cart not found, creating new cart for userId: " + userId);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Cart newCart = new Cart();
            newCart.setUser(user);
            Cart savedCart = cartRepository.save(newCart);
            System.out.println("New cart created with id: " + savedCart.getId());
            return savedCart;
        }
        System.out.println("Cart found with id: " + cartOpt.get().getId());
        return cartOpt.get();
    }

    public Cart addProductToCart(Long userId, Long productId) {
        System.out.println("Adding productId: " + productId + " to cart for userId: " + userId);
        Cart cart = getCartByUser(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        System.out.println("Product found: " + product.getName());
        cart.getProducts().add(product);
        System.out.println("Product added to cart, saving cart...");
        // Добавляем товар в корзину (логика увеличения количества уже в Cart.addProduct)
        cart.addProduct(product);
        System.out.println("Product added to cart, saving cart...");

        Cart savedCart = cartRepository.save(cart);
        System.out.println("Cart saved successfully with id: " + savedCart.getId());
        return savedCart;
    }

    public Cart removeProductFromCart(Long userId, Long productId) {
        System.out.println("Removing productId: " + productId + " from cart for userId: " + userId);
        Cart cart = getCartByUser(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        cart.removeProduct(product);
        return cartRepository.save(cart);
    }

    public void clearCart(Long userId) {
        System.out.println("Clearing cart for userId: " + userId);
        Cart cart = getCartByUser(userId);
        cart.clearProducts();
        cartRepository.save(cart);
    }
}