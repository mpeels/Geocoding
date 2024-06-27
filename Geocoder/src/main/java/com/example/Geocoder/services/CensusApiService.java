package com.example.Geocoder.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.example.Geocoder.requests.AddressRequest;
import com.example.Geocoder.responses.AddressMatchResponse;
import com.example.Geocoder.responses.CensusApiResponse;


/**
 * The service layer for the Geocoder application. This class is responsible for
 * making requests to the US Census Geocoder API and returning the response as JSON.
 * 
 * <p> Use Case: Validate an American address as well as return longitude and latitude<p>
 * <p> Return: JSON response from US Census API<p>
 * @author Jabir Emeka
 * @version 2.0
 * @param address
 */

@Service
public class CensusApiService {

    private final RestClient restClient;
    
    /**Initializies restClient for CensusService */
    public CensusApiService(RestClient.Builder restClientBuilder){
        this.restClient = restClientBuilder
            .baseUrl("https://geocoding.geo.census.gov/geocoder/locations/address")
            .build(); 
    }

    /**Makes API request call with crafted uri from Address object. Returned as response.*/
    public CensusApiResponse submitAddress(AddressRequest addressRequests){
        CensusApiResponse response = restClient.get()
            .uri("?street={street}&city={city}&state={state}&zip={zip}&benchmark=Public_AR_Current&format=json", 
            addressRequests.street(), 
            addressRequests.city(), 
            addressRequests.state(), 
            addressRequests.zip())
            .retrieve()
            .body(CensusApiResponse.class);

        if(response.result().addressMatches().isEmpty()){
            response.result().addressMatches().add( new AddressMatchResponse("Address not found"));
        }

        return response;
    }
}
