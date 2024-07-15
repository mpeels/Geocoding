package com.example.geocoder.services;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.example.geocoder.exceptions.RateLimitExceededException;
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
   * response.
   */

  @Cacheable("censusApiResponseCache")
  public CensusApiResponse submitAddress(AddressRequest addressRequests) {
    //check the addres parameters

    CensusApiResponse response = restClient.get()
        .uri("?street={street}&city={city}&state={state}&zip={zip}&benchmark=Public_AR_Current&format=json",
            addressRequests.street(),
            addressRequests.city(),
            addressRequests.state(),
            addressRequests.zip())
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, (request, result) -> {
          throw new RuntimeException(); //Placeholder
        })
        .body(CensusApiResponse.class);


    
    if (response.result().addressMatches().isEmpty()) {
      throw new RuntimeException(); //Placeholder
    }

    return response;
  }
}
