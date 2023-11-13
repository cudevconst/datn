package com.example.demo.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
//    private final String CLOUD_NAME = "YOUR_CLOUD_NAME";
//    private final String API_KEY = "YOUR_API_KEY";
//    private final String API_SECRET = "YOUR_SECRET_KEY";
    @Bean
    public Cloudinary cloudinary(){
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name","");
        config.put("api_key","");
        config.put("api_secret","");

        return new Cloudinary(config);
    }
}
