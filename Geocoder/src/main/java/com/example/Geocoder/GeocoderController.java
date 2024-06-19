package com.example.Geocoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/address")
class GeocoderController{
    private final CensusService censusService;

    @Autowired //Injecting an instance of the CensusService
    public GeocoderController(CensusService censusService){
        this.censusService = censusService;
    }

    //ReqeustBody deserializes the JSON mapping the request body to an Address object (https://www.baeldung.com/spring-request-response-body)
    //A ResponseEntity represents the HTTP Response, allowing for the status code, headers and body to be set manually (https://www.baeldung.com/spring-response-entity)
    @PostMapping
    public Mono<ResponseEntity<String>> submitAddress(@RequestBody Address address){
        return censusService.submitAddress(address)
            .map(response -> ResponseEntity.ok(response)) //Maps the output of censusService to a ResponseEntity with a status of OK
            .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Address not found")); //or returns a ResponseEntity with 404 if NOT FOUND
    }
}