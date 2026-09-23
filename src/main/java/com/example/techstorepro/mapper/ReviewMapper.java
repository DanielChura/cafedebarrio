package com.example.techstorepro.mapper;

import com.example.techstorepro.dto.request.ReviewRequest;
import com.example.techstorepro.dto.response.ReviewResponse;
import com.example.techstorepro.entity.Product;
import com.example.techstorepro.entity.Review;
import com.example.techstorepro.entity.User;

public final class ReviewMapper {

    private ReviewMapper() {
    }

    public static Review toEntity(ReviewRequest request, User user, Product product) {
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        return review;
    }

    public static ReviewResponse toResponse(Review review) {
        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        if (review.getUser() != null) {
            response.setUserId(review.getUser().getId());
            response.setUserName(review.getUser().getName());
        }
        if (review.getProduct() != null) {
            response.setProductId(review.getProduct().getId());
            response.setProductName(review.getProduct().getName());
        }
        response.setRating(review.getRating());
        response.setComment(review.getComment());
        response.setCreatedAt(review.getCreatedAt());
        return response;
    }
}
