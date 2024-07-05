package com.example.Geocoder.services;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.example.Geocoder.requests.AddressRequest;
import com.example.Geocoder.responses.AddressMatchResponse;
import com.example.Geocoder.responses.CensusApiResponse;
import com.example.Geocoder.responses.ResultResponse;

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
    public CensusApiService(RestClient restClient){
        this.restClient = restClient;
    }

    /**Makes API request call with crafted uri from Address object. Returned as response.*/
    public CensusApiResponse submitAddress(AddressRequest addressRequests){
        // HttpStatusCode clientServerCode;
        final HttpStatusCode[] clientServerCode = new HttpStatusCode[1];
        
        CensusApiResponse response = restClient.get()
            .uri("?street={street}&city={city}&state={state}&zip={zip}&benchmark=Public_AR_Current&format=json", 
            addressRequests.street(), 
            addressRequests.city(), 
            addressRequests.state(), 
            addressRequests.zip())
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, (request, result) -> {
                clientServerCode[0] = result.getStatusCode();  
            })
            .body(CensusApiResponse.class);
        
        if(clientServerCode[0] != null && clientServerCode[0].is4xxClientError()){
            String error = clientServerCode[0].toString() + ": Street address cannot be empty and cannot exceed 100 characters, Specify House number and Street name along with City and State and/or ZIP Code" ;
            AddressMatchResponse addressMatch = new AddressMatchResponse(error);
            ResultResponse result = new ResultResponse(addressMatch);

            return new CensusApiResponse(result);
        }
        else if(response.result().addressMatches().isEmpty()){
            response.result().addressMatches().add( new AddressMatchResponse("Address not found"));
        }
       
        return response;
    }
}
