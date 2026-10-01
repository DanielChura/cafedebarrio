package com.example.softdevoluciones.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.softdevoluciones.dto.request.ReviewRequest;
import com.example.softdevoluciones.dto.response.ReviewResponse;
import com.example.softdevoluciones.entity.Product;
import com.example.softdevoluciones.entity.Review;
import com.example.softdevoluciones.entity.User;
import com.example.softdevoluciones.exception.BadRequestException;
import com.example.softdevoluciones.exception.ResourceNotFoundException;
import com.example.softdevoluciones.mapper.ReviewMapper;
import com.example.softdevoluciones.repository.OrderDetailRepository;
import com.example.softdevoluciones.repository.ProductRepository;
import com.example.softdevoluciones.repository.ReviewRepository;
import com.example.softdevoluciones.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

        private final ReviewRepository reviewRepository;
        private final OrderDetailRepository orderDetailRepository;
        private final UserRepository userRepository;
        private final ProductRepository productRepository;

        public Page<ReviewResponse> findByProduct(UUID productId, Pageable pageable) {
                if (!productRepository.existsById(productId)) {
                        throw new ResourceNotFoundException(
                                        "El producto solicitado no existe o ya no está disponible. Por favor, verifica la información e inténtalo de nuevo.");
                }
                return reviewRepository.findByProduct_Id(productId, pageable)
                                .map(ReviewMapper::toResponse);
        }

        @Transactional
        public ReviewResponse create(UUID userId, ReviewRequest request) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "El usuario solicitado no existe o ya fue eliminado. Por favor, verifica la información e inténtalo de nuevo."));
                Product product = productRepository.findById(request.getProductId())
                                .orElseThrow(
                                                () -> new ResourceNotFoundException(
                                                                "El producto solicitado no existe o ya no está disponible. Por favor, verifica la información e inténtalo de nuevo."));

                if (!orderDetailRepository.existsByOrder_User_IdAndProduct_Id(user.getId(), product.getId())) {
                        throw new BadRequestException(
                                        "Solo los usuarios que compraron este producto pueden dejar una reseña. Debes completar una compra del producto antes de poder valorarlo.");
                }
                if (reviewRepository.existsByUser_IdAndProduct_Id(user.getId(), product.getId())) {
                        throw new BadRequestException(
                                        "Ya has publicado una reseña para este producto. No es posible registrar más de una reseña por producto y usuario.");
                }

                Review review = ReviewMapper.toEntity(request, user, product);
                return ReviewMapper.toResponse(reviewRepository.save(review));
        }

        @Transactional
        public void deleteById(UUID id) {
                Review review = reviewRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "La reseña solicitada no existe o ya fue eliminada. Por favor, verifica la información e inténtalo de nuevo."));
                reviewRepository.delete(review);
        }
}
