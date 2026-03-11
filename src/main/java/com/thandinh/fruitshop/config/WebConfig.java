package com.thandinh.fruitshop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configure CORS to allow mobile app to call API
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*") // Allow all origins (for development)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600);
        
        System.out.println("✅ CORS enabled for /api/** endpoints - Mobile app can now call API");
    }

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
