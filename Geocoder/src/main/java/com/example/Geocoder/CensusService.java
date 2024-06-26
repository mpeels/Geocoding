package com.example.Geocoder;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import reactor.core.publisher.Mono;

/**
 * The service layer for the Geocoder application. This class is responsible for
 * making requests to the US Census Geocoder API and returning the response as JSON.
 * 
 * <p> Use Case: Validate an American address as well as return longitude and latitude<p>
 * <p> Return: JSON response from US Census API<p>
 * @author Jabir Emeka
 * @version 1.0
 * @param address
 */

@Service
public class CensusService {

    private final WebClient webClient;
    private final DefaultUriBuilderFactory uriBuilderFactory;

    // https://geocoding.geo.census.gov/geocoder/Geocoding_Services_API.pdf
    // https://www.baeldung.com/spring-autowire

    @Autowired //injecting the WebClient.Builder
    public CensusService(WebClient.Builder webClientBuilder){
        this.uriBuilderFactory = new DefaultUriBuilderFactory("https://geocoding.geo.census.gov/geocoder/locations/address");
        this.webClient = webClientBuilder.uriBuilderFactory(uriBuilderFactory).build();
    }

    //A Container that emits 0 to 1 items. Mono allows for asynchronous development that improve efficieny, responsiveness 
    //and resource utilization.
    public Mono<String> submitAddress(Address address){

        //https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/util/DefaultUriBuilderFactory.html
        //https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/util/UriBuilder.html

        //Allows the creation of UriBuilder instances with a common base URL
        //I choose not to use UriComponentsBuilder because I am working with a base URL being the Census API.
        URI uri = uriBuilderFactory.builder()
            .queryParam("street", address.street())
            .queryParam("city", address.city())
            .queryParam("state", address.state())
            .queryParam("zip", address.zip())
            .queryParam("benchmark", "Public_AR_Current")
            .queryParam("format", "json")
            .build();

        //Makes a get request to the uri and returns the response body as a Mono type String.
        return webClient.get()
            .uri(uri)
            .retrieve()
            .bodyToMono(String.class);

    }

}
