package com.example.geocoder.services;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.example.geocoder.exceptions.InvalidAddressException;
import com.example.geocoder.exceptions.MissingStreetException;
import com.example.geocoder.exceptions.MissingZipOrCityStateException;
import com.example.geocoder.requests.AddressRequest;
import com.example.geocoder.responses.CensusApiResponse;

/**
 * The service layer for the Geocoder application. This class is responsible for
 * making requests to the US Census Geocoder API and returning the response as
 * JSON.
 * 
 * <p>
 * Use Case: Validate an American address as well as return longitude and
 * latitude
 * <p>
 * <p>
 * Return: JSON response from US Census API
 * <p>
 * 
 * @author Jabir Emeka
 * @version 2.0
 * @param address
 */

@Service
public class CensusApiService {

  private final RestClient restClient;

  /** Initializies restClient for CensusService */
  public CensusApiService(RestClient restClient) {
    this.restClient = restClient;
  }

  /**
   * Makes API request call with crafted uri from Address object. Returned as
   * Census API Response object.
   */

  @Cacheable("censusApiResponseCache")
  public CensusApiResponse submitAddress(AddressRequest addressRequests) {
    validateRequest(addressRequests);
    CensusApiResponse response = restClient.get()
        .uri("?street={street}&city={city}&state={state}&zip={zip}&benchmark=Public_AR_Current&format=json",
            addressRequests.street(),
            addressRequests.city(),
            addressRequests.state(),
            addressRequests.zip())
        .retrieve()
        .body(CensusApiResponse.class);
    
    if (response.result().addressMatches().isEmpty()) {
      throw new InvalidAddressException(); //Placeholder
    }
    return response;
  }

   //Validate Request parameters before api call
   void validateRequest(AddressRequest addressRequests){
    String street = addressRequests.street();
    String city = addressRequests.city();
    String state = addressRequests.state();
    String zip = addressRequests.zip();

    if(street == null || street.trim().equals("")){
      throw new MissingStreetException();
    }
    else if(zip == null || zip.trim().equals("") && 
    (city == null || city.trim().equals("") ||
     state == null || state.trim().equals(""))){
      throw new MissingZipOrCityStateException();
    }
   }
}
