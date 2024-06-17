package com.example.Geocoder;

import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("api/address")
class GeocoderController{
    private final CensusService censusService;

    @Autowired
    public GeocoderController(CensusService censusService){
        this.censusService = censusService;
    }

    @PostMapping
    public Mono<ResponseEntity<String>> fixedAddress(){
        return censusService.returnAddress()
            .map(response -> ResponseEntity.ok(response))
            .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Address not found"));
    }
}