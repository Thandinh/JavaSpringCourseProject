package com.thandinh.fruitshop.service;

import com.thandinh.fruitshop.entity.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private ProductService productService;

    // Cart item class
    public static class CartItem {
        private Long productId;
        private Product product;
        private int quantity;
        private Double price;

        public CartItem(Long productId, Product product, int quantity, Double price) {
            this.productId = productId;
            this.product = product;
            this.quantity = quantity;
            this.price = price;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Double getTotal() {
            return price * quantity;
        }
    }

    /**
     * Get cart from session
     */
    @SuppressWarnings("unchecked")
    public List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    /**
     * Add product to cart
     */
    public void addToCart(HttpSession session, Long productId, int quantity, Double price) {
        List<CartItem> cart = getCart(session);
        Product product = productService.findById(productId).orElse(null);
        
        if (product == null) {
            return;
        }

        // Check if product already in cart
        for (CartItem item : cart) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                updateSession(session, cart);
                return;
            }
        }

        // Add new item
        cart.add(new CartItem(productId, product, quantity, price != null ? price : product.getPricesale()));
        updateSession(session, cart);
    }

    /**
     * Update cart item quantity
     */
    public void updateCartItem(HttpSession session, Long productId, String action) {
        List<CartItem> cart = getCart(session);
        
        for (CartItem item : cart) {
            if (item.getProductId().equals(productId)) {
                if ("increase".equals(action)) {
                    item.setQuantity(item.getQuantity() + 1);
                } else if ("decrease".equals(action)) {
                    if (item.getQuantity() > 1) {
                        item.setQuantity(item.getQuantity() - 1);
                    }
                }
                break;
            }
        }
        
        updateSession(session, cart);
    }

    /**
     * Remove product from cart
     */
    public void removeFromCart(HttpSession session, Long productId) {
        List<CartItem> cart = getCart(session);
        cart.removeIf(item -> item.getProductId().equals(productId));
        updateSession(session, cart);
    }

    /**
     * Clear entire cart
     */
    public void clearCart(HttpSession session) {
        session.setAttribute("cart", new ArrayList<CartItem>());
        session.setAttribute("countCart", 0);
        session.setAttribute("totalCart", 0.0);
    }

    /**
     * Calculate total price
     */
    public Double calculateTotal(List<CartItem> cart) {
        return cart.stream()
                .mapToDouble(CartItem::getTotal)
                .sum();
    }

    /**
     * Get cart item count
     */
    public int getCartCount(List<CartItem> cart) {
        return cart.size();
    }

    /**
     * Update session attributes
     */
    private void updateSession(HttpSession session, List<CartItem> cart) {
        session.setAttribute("cart", cart);
        session.setAttribute("countCart", getCartCount(cart));
        session.setAttribute("totalCart", calculateTotal(cart));
    }
}
