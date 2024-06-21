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

    
    
    // The response from the API call is processed and formatted into before being returned to the user. 
    // Return response object
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
                    
                    Response formattedResponse = new Response(
                        addressMatches.getString("matchedAddress"), 
                        addressMatches.getJSONObject("coordinates").getDouble("x"), 
                        addressMatches.getJSONObject("coordinates").getDouble("y"));

                    return Mono.just(new ResponseEntity<>("Verified Address:\n" + 
                        formattedResponse.matchedAddress() + "\ncoordinates: \n" + formattedResponse.x() + ", " + formattedResponse.y(), HttpStatus.OK));
                } catch (JSONException e) {
                    return Mono.just(new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
                }
            })
            .onErrorResume(e -> Mono.just(new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR)));
      
    }
}