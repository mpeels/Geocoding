package com.example.Geocoder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenAPIConfig {
    @Bean //Instantiates an OpenAPI object to be used in the Swagger-ui
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                    .title("Geocoder API")
                    .version("1.2")
                    .description("Geocoder API for getting the latitude and longitude of a given address"));
    }
}
