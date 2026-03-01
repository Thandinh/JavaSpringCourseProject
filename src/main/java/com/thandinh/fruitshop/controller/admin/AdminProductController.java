package com.thandinh.fruitshop.controller.admin;

import com.thandinh.fruitshop.entity.Category;
import com.thandinh.fruitshop.entity.Product;
import com.thandinh.fruitshop.service.CategoryService;
import com.thandinh.fruitshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    private static final String UPLOAD_DIR = "uploads/products/";

    /**
     * Display list of all products with pagination
     */
    @GetMapping
    public String index(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "id") String sortBy,
                       @RequestParam(defaultValue = "desc") String sortDir,
                       Model model) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Product> productPage = productService.findAll(pageable);
            
            model.addAttribute("productPage", productPage);
            model.addAttribute("productList", productPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", productPage.getTotalPages());
            model.addAttribute("totalItems", productPage.getTotalElements());
            model.addAttribute("pageSize", size);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("sortDir", sortDir);
        } catch (Exception e) {
            List<Product> productList = productService.findAll();
            model.addAttribute("productList", productList);
            model.addAttribute("currentPage", 0);
            model.addAttribute("totalPages", 1);
        }
        
        return "admin/product/index";
    }

    /**
     * Show create product form
     */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("product", new Product());
        return "admin/product/create";
    }

    /**
     * Handle product creation
     */
    @PostMapping("/create")
    public String create(@RequestParam("name") String name,
                        @RequestParam("description") String description,
                        @RequestParam("price") Double price,
                        @RequestParam("pricesale") Double pricesale,
                        @RequestParam("quantity") Integer quantity,
                        @RequestParam("categoryId") Long categoryId,
                        @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
                        RedirectAttributes redirectAttributes) {
        
        try {
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setPricesale(pricesale);
            product.setQuantity(quantity);

            // Set category
            Optional<Category> category = categoryService.findById(categoryId);
            if (category.isPresent()) {
                product.setCategory(category.get());
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Invalid category selected!");
                return "redirect:/admin/products/create";
            }

            // Handle file upload
            if (thumbnail != null && !thumbnail.isEmpty()) {
                String imageUrl = saveUploadedFile(thumbnail);
                product.setImageUrl(imageUrl);
            }

            productService.save(product);
            redirectAttributes.addFlashAttribute("successMessage", "Product created successfully!");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating product: " + e.getMessage());
        }

        return "redirect:/admin/products";
    }

    /**
     * Show edit product form
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Product> productOpt = productService.findById(id);
        
        if (productOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Product not found!");
            return "redirect:/admin/products";
        }

        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("product", productOpt.get());
        model.addAttribute("categoryList", categoryList);
        return "admin/product/edit";
    }

    /**
     * Handle product update
     */
    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Long id,
                        @RequestParam("name") String name,
                        @RequestParam("description") String description,
                        @RequestParam("price") Double price,
                        @RequestParam("pricesale") Double pricesale,
                        @RequestParam("quantity") Integer quantity,
                        @RequestParam("categoryId") Long categoryId,
                        @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
                        @RequestParam(value = "oldImageUrl", required = false) String oldImageUrl,
                        RedirectAttributes redirectAttributes) {
        
        try {
            Optional<Product> productOpt = productService.findById(id);
            
            if (productOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Product not found!");
                return "redirect:/admin/products";
            }

            Product product = productOpt.get();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setPricesale(pricesale);
            product.setQuantity(quantity);

            // Set category
            Optional<Category> category = categoryService.findById(categoryId);
            if (category.isPresent()) {
                product.setCategory(category.get());
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Invalid category selected!");
                return "redirect:/admin/products/edit/" + id;
            }

            // Handle file upload
            if (thumbnail != null && !thumbnail.isEmpty()) {
                // Delete old image if exists
                if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
                    deleteOldFile(oldImageUrl);
                }
                String imageUrl = saveUploadedFile(thumbnail);
                product.setImageUrl(imageUrl);
            }

            productService.save(product);
            redirectAttributes.addFlashAttribute("successMessage", "Product updated successfully!");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating product: " + e.getMessage());
        }

        return "redirect:/admin/products";
    }

    /**
     * Delete product
     */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Product> productOpt = productService.findById(id);
            
            if (productOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Product not found!");
                return "redirect:/admin/products";
            }

            Product product = productOpt.get();
            
            // Delete image file if exists
            if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                deleteOldFile(product.getImageUrl());
            }

            productService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully!");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting product: " + e.getMessage());
        }

        return "redirect:/admin/products";
    }

    /**
     * Save uploaded file to server
     */
    private String saveUploadedFile(MultipartFile file) throws IOException {
        // Create upload directory if not exists
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdirs();
            System.out.println("Upload directory created: " + uploadDir.getAbsolutePath() + " - Success: " + created);
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".") 
                ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
                : "";
        String uniqueFilename = UUID.randomUUID().toString() + extension;

        // Save file
        Path filePath = Paths.get(UPLOAD_DIR + uniqueFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        System.out.println("File saved to: " + filePath.toAbsolutePath());
        System.out.println("File URL: /uploads/products/" + uniqueFilename);

        // Return relative URL
        return "/uploads/products/" + uniqueFilename;
    }

    /**
     * Delete old file from server
     */
    private void deleteOldFile(String imageUrl) {
        try {
            if (imageUrl != null && imageUrl.startsWith("/uploads/products/")) {
                String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                Path filePath = Paths.get(UPLOAD_DIR + filename);
                Files.deleteIfExists(filePath);
            }
        } catch (IOException e) {
            // Log error but don't throw exception
            System.err.println("Error deleting old file: " + e.getMessage());
        }
    }
}
