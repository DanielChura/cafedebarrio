package com.example.softdevoluciones.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.softdevoluciones.dto.request.CartItemRequest;
import com.example.softdevoluciones.dto.response.CartResponse;
import com.example.softdevoluciones.entity.Cart;
import com.example.softdevoluciones.entity.CartItem;
import com.example.softdevoluciones.entity.Product;
import com.example.softdevoluciones.entity.User;
import com.example.softdevoluciones.exception.BadRequestException;
import com.example.softdevoluciones.exception.ResourceNotFoundException;
import com.example.softdevoluciones.mapper.CartMapper;
import com.example.softdevoluciones.repository.CartRepository;
import com.example.softdevoluciones.repository.ProductRepository;
import com.example.softdevoluciones.repository.UserRepository;

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
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "No se encontró el usuario asociado al carrito. Es posible que la cuenta haya sido eliminada o que el acceso ya no sea válido."));
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }

    private CartItem getItem(Cart cart, UUID productId) {
        return cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El producto solicitado no se encuentra en tu carrito. Es posible que ya lo hayas eliminado o que aún no lo hayas agregado."));
    }

    private Product getProduct(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El producto solicitado no existe o ya no está disponible. Por favor, verifica el catálogo e inténtalo de nuevo."));
        if (!product.getActive()) {
            throw new BadRequestException("El producto '" + product.getName()
                    + "' no está disponible actualmente. Por favor, retíralo del carrito o elige otro producto del catálogo.");
        }
        return product;
    }

    private void checkStock(Product product, int quantity) {
        if (product.getStock() < quantity) {
            throw new BadRequestException("No hay stock suficiente para el producto '" + product.getName()
                    + "'. Stock disponible: " + product.getStock() + ", cantidad solicitada: " + quantity
                    + ". Por favor, ajusta la cantidad e inténtalo de nuevo.");
        }
    }
}
