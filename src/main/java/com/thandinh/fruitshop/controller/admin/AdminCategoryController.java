package com.thandinh.fruitshop.controller.admin;

import com.thandinh.fruitshop.entity.Category;
import com.thandinh.fruitshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * Admin Category Controller
 * Handles category management for admin users
 */
@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Display list of all categories
     */
    @GetMapping
    public String index(Model model) {
        try {
            List<Category> categoryList = categoryService.findAll();
            model.addAttribute("categoryList", categoryList);
            model.addAttribute("totalCategories", categoryList.size());
            return "admin/category/index";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading categories: " + e.getMessage());
            return "admin/category/index";
        }
    }

    /**
     * Show create category form
     */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/category/create";
    }

    /**
     * Create new category
     */
    @PostMapping("/create")
    public String create(@ModelAttribute Category category, 
                        RedirectAttributes redirectAttributes) {
        try {
            categoryService.create(category);
            redirectAttributes.addFlashAttribute("success", "Category created successfully!");
            return "redirect:/admin/categories";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/categories/create";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating category: " + e.getMessage());
            return "redirect:/admin/categories/create";
        }
    }

    /**
     * Show edit category form
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Optional<Category> categoryOpt = categoryService.findById(id);
            
            if (categoryOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Category not found with ID: " + id);
                return "redirect:/admin/categories";
            }
            
            model.addAttribute("category", categoryOpt.get());
            return "admin/category/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error loading category: " + e.getMessage());
            return "redirect:/admin/categories";
        }
    }

    /**
     * Update existing category
     */
    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, 
                        @ModelAttribute Category category,
                        RedirectAttributes redirectAttributes) {
        try {
            categoryService.update(id, category);
            redirectAttributes.addFlashAttribute("success", "Category updated successfully!");
            return "redirect:/admin/categories";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/categories/edit/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating category: " + e.getMessage());
            return "redirect:/admin/categories/edit/" + id;
        }
    }

    /**
     * Delete category
     */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Category deleted successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting category: " + e.getMessage());
        }
        return "redirect:/admin/categories";
    }

    /**
     * Delete category using GET (for compatibility)
     */
    @GetMapping("/delete/{id}")
    public String deleteViaGet(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return delete(id, redirectAttributes);
    }
}
