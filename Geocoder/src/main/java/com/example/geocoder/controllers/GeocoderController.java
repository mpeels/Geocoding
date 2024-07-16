package com.example.geocoder.controllers;

import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.geocoder.exceptions.RateLimitExceededException;
import com.example.geocoder.requests.AddressRequest;
import com.example.geocoder.responses.CensusApiResponse;
import com.example.geocoder.services.CensusApiService;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;

/**
 * The controller for the Gecoder API. This controller builds an instances of
 * the CensusService
 * class. Then gathers user submitted address and routes the US Census API
 * result to the user.
 * Returning as a Response object.
 * 
 * <p>
 * Use Case: Validate an American address as well as return longitude and
 * latitude
 * <p>
 * <p>
 * Return: Response Object from Census Service
 * <p>
 * 
 * @author Jabir Emeka
 * @version 2.0
 * @param address
 */

@RestController
@RequestMapping("/api/address")
class GeocoderController {

  private final CensusApiService censusService;
  private final RateLimiter rateLimiter;
  private final CacheManager cacheManager;

  public GeocoderController(CensusApiService censusService, RateLimiterRegistry rateLimiterRegistry, CacheManager cacheManager) {
    this.censusService = censusService;
    this.rateLimiter = rateLimiterRegistry.rateLimiter("censusApiLimiter");
    this.cacheManager = cacheManager;
  }

  @PostMapping
  public CensusApiResponse submittedAddress(@RequestBody AddressRequest addressRequest) {
    CaffeineCache cache = (CaffeineCache)cacheManager.getCache("censusApiResponseCache");

    if(!cache.getNativeCache().asMap().containsKey(addressRequest) && !rateLimiter.acquirePermission()){
      throw new RateLimitExceededException();
    }
    
    return censusService.submitAddress(addressRequest);
  }
}