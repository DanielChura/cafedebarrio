package com.example.techstorepro.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.techstorepro.dto.request.CartItemRequest;
import com.example.techstorepro.dto.response.CartResponse;
import com.example.techstorepro.entity.Cart;
import com.example.techstorepro.entity.CartItem;
import com.example.techstorepro.entity.Product;
import com.example.techstorepro.entity.User;
import com.example.techstorepro.exception.BadRequestException;
import com.example.techstorepro.exception.ResourceNotFoundException;
import com.example.techstorepro.mapper.CartMapper;
import com.example.techstorepro.repository.CartRepository;
import com.example.techstorepro.repository.ProductRepository;
import com.example.techstorepro.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public CartResponse getByUserId(UUID userId) {
        return CartMapper.toResponse(getCart(userId));
    }

    @Transactional
    public CartResponse addItem(UUID userId, CartItemRequest request) {
        Cart cart = getCart(userId);
        Product product = getProduct(request.getProductId());

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        int quantity = (item == null ? 0 : item.getQuantity()) + request.getQuantity();
        checkStock(product, quantity);

        if (item == null) {
            item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            cart.getItems().add(item);
        }
        item.setQuantity(quantity);
        return CartMapper.toResponse(cartRepository.save(cart));
    }

    @Transactional
    public CartResponse updateItem(UUID userId, CartItemRequest request) {
        Cart cart = getCart(userId);
        CartItem item = getItem(cart, request.getProductId());
        checkStock(item.getProduct(), request.getQuantity());
        item.setQuantity(request.getQuantity());
        return CartMapper.toResponse(cartRepository.save(cart));
    }

    @Transactional
    public CartResponse removeItem(UUID userId, UUID productId) {
        Cart cart = getCart(userId);
        CartItem item = getItem(cart, productId);
        cart.getItems().remove(item);
        return CartMapper.toResponse(cartRepository.save(cart));
    }

    @Transactional
    public CartResponse clear(UUID userId) {
        Cart cart = getCart(userId);
        cart.getItems().clear();
        return CartMapper.toResponse(cartRepository.save(cart));
    }

    private Cart getCart(UUID userId) {
        return cartRepository.findByUser_Id(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }

    private CartItem getItem(Cart cart, UUID productId) {
        return cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in cart with ID: " + productId));
    }

    private Product getProduct(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
        if (!product.getActive()) {
            throw new BadRequestException("Product '" + product.getName() + "' is currently unavailable");
        }
        return product;
    }

    private void checkStock(Product product, int quantity) {
        if (product.getStock() < quantity) {
            throw new BadRequestException("Insufficient stock for product '" + product.getName()
                    + "'. Available stock: " + product.getStock() + ", Requested: " + quantity);
        }
    }
}
