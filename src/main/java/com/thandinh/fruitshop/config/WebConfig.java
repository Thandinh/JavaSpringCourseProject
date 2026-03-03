package com.thandinh.fruitshop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map /uploads/** to BOTH classpath (for user avatars) AND external directory (for products)
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(
                    "classpath:/static/uploads/",  // For user avatars
                    "file:uploads/"                 // For product images (external)
                );
        
        System.out.println("Upload directory configured: classpath:/static/uploads/ and file:uploads/");
        
        // Also serve static resources from classpath (for old products)
        // This allows /assets/images/** to work for existing products
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/");
        
        System.out.println("Static assets directory configured");
    }
}
