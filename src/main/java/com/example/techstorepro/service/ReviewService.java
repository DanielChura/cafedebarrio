package com.example.techstorepro.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.techstorepro.dto.request.ReviewRequest;
import com.example.techstorepro.dto.response.ReviewResponse;
import com.example.techstorepro.entity.Product;
import com.example.techstorepro.entity.Review;
import com.example.techstorepro.entity.User;
import com.example.techstorepro.exception.BadRequestException;
import com.example.techstorepro.exception.ResourceNotFoundException;
import com.example.techstorepro.mapper.ReviewMapper;
import com.example.techstorepro.repository.OrderDetailRepository;
import com.example.techstorepro.repository.ProductRepository;
import com.example.techstorepro.repository.ReviewRepository;
import com.example.techstorepro.repository.UserRepository;

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
            throw new ResourceNotFoundException("Product not found with ID: " + productId);
        }
        return reviewRepository.findByProduct_Id(productId, pageable)
                .map(ReviewMapper::toResponse);
    }

    @Transactional
    public ReviewResponse create(UUID userId, ReviewRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product not found with ID: " + request.getProductId()));

        if (!orderDetailRepository.existsByOrder_User_IdAndProduct_Id(user.getId(), product.getId())) {
            throw new BadRequestException("Only users who purchased this product can review it");
        }
        if (reviewRepository.existsByUser_IdAndProduct_Id(user.getId(), product.getId())) {
            throw new BadRequestException("User has already reviewed this product");
        }

        Review review = ReviewMapper.toEntity(request, user, product);
        return ReviewMapper.toResponse(reviewRepository.save(review));
    }

    @Transactional
    public void deleteById(UUID id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + id));
        reviewRepository.delete(review);
    }
}
