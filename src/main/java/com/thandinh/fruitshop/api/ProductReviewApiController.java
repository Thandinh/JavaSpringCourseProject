package com.thandinh.fruitshop.api;

import com.thandinh.fruitshop.dto.CreateReviewDTO;
import com.thandinh.fruitshop.dto.ProductReviewDTO;
import com.thandinh.fruitshop.entity.Product;
import com.thandinh.fruitshop.entity.ProductReview;
import com.thandinh.fruitshop.entity.User;
import com.thandinh.fruitshop.repository.ProductRepository;
import com.thandinh.fruitshop.repository.ProductReviewRepository;
import com.thandinh.fruitshop.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products/{productId}/reviews")
@CrossOrigin(origins = "*")
public class ProductReviewApiController {

    @Autowired
    private ProductReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get all reviews for a product
     * GET /api/products/{productId}/reviews
     */
    @GetMapping
    public ResponseEntity<?> getProductReviews(@PathVariable Long productId) {
        try {
            List<ProductReviewDTO> reviews = reviewRepository.findByProductIdOrderByCreatedAtDesc(productId)
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            Double averageRating = reviewRepository.getAverageRating(productId);
            Long totalReviews = reviewRepository.getReviewCount(productId);

            Map<String, Object> response = new HashMap<>();
            response.put("reviews", reviews);
            response.put("averageRating", averageRating != null ? Math.round(averageRating * 10.0) / 10.0 : 0.0);
            response.put("totalReviews", totalReviews != null ? totalReviews : 0);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Create a new review
     * POST /api/products/{productId}/reviews
     */
    @PostMapping
    public ResponseEntity<?> createReview(
            @PathVariable Long productId,
            @Valid @RequestBody CreateReviewDTO createReviewDTO) {
        
        try {
            User user = getCurrentUser();
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Vui lòng đăng nhập để đánh giá"));
            }

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

            // Check if user already reviewed this product
            if (reviewRepository.existsByProductIdAndUserId(productId, user.getId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Bạn đã đánh giá sản phẩm này rồi"));
            }

            ProductReview review = new ProductReview();
            review.setProduct(product);
            review.setUser(user);
            review.setRating(createReviewDTO.getRating());
            review.setComment(createReviewDTO.getComment());

            ProductReview savedReview = reviewRepository.save(review);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Đánh giá thành công",
                    "review", convertToDTO(savedReview)
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Update a review
     * PUT /api/products/{productId}/reviews/{reviewId}
     */
    @PutMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(
            @PathVariable Long productId,
            @PathVariable Long reviewId,
            @Valid @RequestBody CreateReviewDTO updateReviewDTO) {
        
        try {
            User user = getCurrentUser();
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Vui lòng đăng nhập"));
            }

            ProductReview review = reviewRepository.findById(reviewId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đánh giá"));

            // Check if user owns this review
            if (!review.getUser().getId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Bạn không có quyền chỉnh sửa đánh giá này"));
            }

            review.setRating(updateReviewDTO.getRating());
            review.setComment(updateReviewDTO.getComment());

            ProductReview savedReview = reviewRepository.save(review);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Cập nhật đánh giá thành công",
                    "review", convertToDTO(savedReview)
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Delete a review
     * DELETE /api/products/{productId}/reviews/{reviewId}
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @PathVariable Long productId,
            @PathVariable Long reviewId) {
        
        try {
            User user = getCurrentUser();
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Vui lòng đăng nhập"));
            }

            ProductReview review = reviewRepository.findById(reviewId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đánh giá"));

            // Check if user owns this review
            if (!review.getUser().getId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Bạn không có quyền xóa đánh giá này"));
            }

            reviewRepository.delete(review);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Xóa đánh giá thành công"
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return null;
        }

        String email = auth.getName();
        return userRepository.findByEmailAndDeletedAtIsNull(email).orElse(null);
    }

    private ProductReviewDTO convertToDTO(ProductReview review) {
        User user = review.getUser();
        return new ProductReviewDTO(
                review.getId(),
                review.getProduct().getId(),
                user.getId(),
                user.getFullName(),
                user.getAvatar(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt(),
                review.getUpdatedAt()
        );
    }
}
