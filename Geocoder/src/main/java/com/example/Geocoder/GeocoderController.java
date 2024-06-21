package com.example.Geocoder;
import org.json.JSONException;
import org.json.JSONObject;
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
    
    // The response from the API call is processed and formatted into before being returned to the user. 
    @PostMapping
    public Mono<ResponseEntity<String>> submitAddress(@RequestBody Address address){
        return censusService.submitAddress(address)
            .flatMap(response ->  {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONObject addressMatches = jsonResponse
                        .getJSONObject("result")
                        .getJSONArray("addressMatches")
                        .getJSONObject(0);
                    
                    
                    String matchedAddress = addressMatches.getString("matchedAddress");
                    double x = addressMatches.getJSONObject("coordinates").getDouble("x");
                    double y = addressMatches.getJSONObject("coordinates").getDouble("y");

                    return Mono.just(new ResponseEntity<>("Verified Address:\n" + 
                        matchedAddress + "\ncoordinates: \n" + x + ", " + y, HttpStatus.OK));
                } catch (JSONException e) {
                    return Mono.just(new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
                }
            })
            .onErrorResume(e -> Mono.just(new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR)));
      
    }
}