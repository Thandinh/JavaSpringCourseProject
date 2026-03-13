package com.thandinh.fruitshop.api;

import com.thandinh.fruitshop.dto.WishlistDTO;
import com.thandinh.fruitshop.entity.Product;
import com.thandinh.fruitshop.entity.User;
import com.thandinh.fruitshop.entity.Wishlist;
import com.thandinh.fruitshop.repository.ProductRepository;
import com.thandinh.fruitshop.repository.UserRepository;
import com.thandinh.fruitshop.repository.WishlistRepository;
import jakarta.transaction.Transactional;
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
@RequestMapping("/api/wishlist")
@CrossOrigin(origins = "*")
public class WishlistApiController {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Get all wishlist items for current user
     * GET /api/wishlist
     */
    @GetMapping
    public ResponseEntity<?> getWishlist() {
        try {
            User user = getCurrentUser();
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Vui lòng đăng nhập"));
            }

            List<WishlistDTO> wishlist = wishlistRepository.findByUserId(user.getId())
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "wishlist", wishlist
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Add product to wishlist
     * POST /api/wishlist/{productId}
     */
    @PostMapping("/{productId}")
    public ResponseEntity<?> addToWishlist(@PathVariable Long productId) {
        try {
            User user = getCurrentUser();
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Vui lòng đăng nhập"));
            }

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

            // Check if already in wishlist
            if (wishlistRepository.existsByUserIdAndProductId(user.getId(), productId)) {
                return ResponseEntity.ok(Map.of(
                        "success", false,
                        "message", "Sản phẩm đã có trong danh sách yêu thích"
                ));
            }

            Wishlist wishlist = new Wishlist();
            wishlist.setUser(user);
            wishlist.setProduct(product);
            wishlistRepository.save(wishlist);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Đã thêm vào danh sách yêu thích"
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Remove product from wishlist
     * DELETE /api/wishlist/{productId}
     */
    @DeleteMapping("/{productId}")
    @Transactional
    public ResponseEntity<?> removeFromWishlist(@PathVariable Long productId) {
        try {
            User user = getCurrentUser();
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Vui lòng đăng nhập"));
            }

            wishlistRepository.deleteByUserIdAndProductId(user.getId(), productId);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Đã xóa khỏi danh sách yêu thích"
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Check if product is in wishlist
     * GET /api/wishlist/check/{productId}
     */
    @GetMapping("/check/{productId}")
    public ResponseEntity<?> checkWishlist(@PathVariable Long productId) {
        try {
            User user = getCurrentUser();
            if (user == null) {
                return ResponseEntity.ok(Map.of("inWishlist", false));
            }

            boolean inWishlist = wishlistRepository.existsByUserIdAndProductId(user.getId(), productId);

            return ResponseEntity.ok(Map.of("inWishlist", inWishlist));

        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("inWishlist", false));
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

    private WishlistDTO convertToDTO(Wishlist wishlist) {
        Product product = wishlist.getProduct();
        return new WishlistDTO(
                wishlist.getId(),
                product.getId(),
                product.getName(),
                product.getSlug(),
                product.getPrice(),
                product.getPricesale(),
                product.getFullImageUrl(),
                product.getCategory() != null ? product.getCategory().getName() : null,
                product.getQuantity(),
                wishlist.getCreatedAt()
        );
    }
}
