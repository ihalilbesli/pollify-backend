package com.pollify.pollify.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // tüm endpointlere izin ver
                .allowedOriginPatterns("http://localhost:4200") // Angular için
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true); // JWT cookie/token taşınacaksa true olmalı
    }
}