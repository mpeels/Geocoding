package com.example.Geocoder.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Geocoder.services.CensusApiService;
import com.example.Geocoder.responses.CensusApiResponse;
import com.example.Geocoder.requests.AddressRequest;

/**
 * The controller for the Gecoder API. This controller builds an instances of the CensusService
 * class. Then gathers user submitted address and routes the US Census API result to the user.
 * Returning as a Response object.
 * 
 * <p> Use Case: Validate an American address as well as return longitude and latitude<p>
 * <p> Return: Response Object from Census Service<p>
 * @author Jabir Emeka
 * @version 2.0
 * @param address
 */

@RestController
@RequestMapping("/api/address")
class GeocoderController{

    private final CensusApiService censusService;

    public GeocoderController(CensusApiService censusService){
        this.censusService = censusService;
    }

    @PostMapping
    public CensusApiResponse submittedAddress(@RequestBody AddressRequest addressRequest){
        return censusService.submitAddress(addressRequest);
    }
}