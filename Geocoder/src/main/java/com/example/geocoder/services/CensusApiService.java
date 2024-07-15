package com.example.geocoder.services;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.example.geocoder.requests.AddressRequest;
import com.example.geocoder.responses.AddressMatchResponse;
import com.example.geocoder.responses.CensusApiResponse;
import com.example.geocoder.responses.ResultResponse;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;


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
  private final RateLimiter rateLimiter;

  /** Initializies restClient for CensusService */
  public CensusApiService(RestClient restClient, RateLimiterRegistry rateLimiterRegistry) {
    this.restClient = restClient;
    this.rateLimiter = rateLimiterRegistry.rateLimiter("censusApiLimiter");
  }

  /**
   * Makes API request call with crafted uri from Address object. Returned as
   * response.
   */

  @Cacheable(value = "censusApiResponseCache", unless="#result.rateLimitPass == false")
  public CensusApiResponse submitAddress(AddressRequest addressRequests) {

    if(!rateLimiter.acquirePermission()){
      return rateLimitFallback();
    }

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


    if (clientServerCode[0] != null && clientServerCode[0].is4xxClientError()) {
      String error = clientServerCode[0].toString()
          + ": Street address cannot be empty and cannot exceed 100 characters, Specify House number and Street name along with City and State and/or ZIP Code";
      AddressMatchResponse addressMatch = new AddressMatchResponse(error);
      ResultResponse result = new ResultResponse(addressMatch);

      return new CensusApiResponse(result, true);
    }
    else if (response.getResult().addressMatches().isEmpty()) {
      response.getResult().addressMatches().add(new AddressMatchResponse("Address not found"));
    }

    response.setRateLimitPass(true);
    return response;
  }

  public CensusApiResponse rateLimitFallback(){
    String error = "Rate limit exceeded. Please try again later.";
    AddressMatchResponse addressMatch = new AddressMatchResponse(error);
    ResultResponse result = new ResultResponse(addressMatch);
    return new CensusApiResponse(result, false);
  }
}
