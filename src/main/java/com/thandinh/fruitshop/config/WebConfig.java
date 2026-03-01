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
        // Get the absolute path to the uploads directory (for new uploads)
        Path uploadDir = Paths.get("uploads").toAbsolutePath().normalize();
        String uploadPath = uploadDir.toUri().toString();
        
        // Map /uploads/** URLs to the uploads directory (for new products)
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath);
        
        System.out.println("Upload directory configured: " + uploadPath);
        
        // Also serve static resources from classpath (for old products)
        // This allows /assets/images/** to work for existing products
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/");
        
        System.out.println("Static assets directory configured for old products");
    }
}
