package com.example.geocoder.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class CensusRestClientConfig {
  @Bean
  public RestClient restClientConfig(RestClient.Builder restClientBuilder) {
    return restClientBuilder
        .baseUrl("https://geocoding.geo.census.gov/geocoder/locations/address")
        .build();
  }
}
