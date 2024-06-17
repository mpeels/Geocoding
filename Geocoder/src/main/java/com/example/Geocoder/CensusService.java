package com.example.Geocoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class CensusService {
  private final WebClient webClient;

  @Autowired
  public CensusService(WebClient.Builder webClientBuilder){
    this.webClient = webClientBuilder.baseUrl("https://geocoding.geo.census.gov/geocoder/locations/address?street=1600%20Pennsylvania%20Avenue%20NW&city=Washington&state=&zip=DC%2020500&benchmark=4&format=json").build();
  }

  public Mono<String> returnAddress(){
    return webClient.get().retrieve().bodyToMono(String.class);
  }

}
