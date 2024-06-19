package com.example.Geocoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


//https://www.youtube.com/watch?v=RHZgllzbjVQ&t=2s
@Configuration //declare that this class is a source of @bean definitions for WebClient 
public class WebClientConfig {
    @Bean
    public WebClient.Builder webClientBuilder(){
        return WebClient.builder();
    }
}
