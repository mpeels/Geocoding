package com.example.geocoder.services;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import com.example.geocoder.exceptions.InvalidAddressException;
import com.example.geocoder.exceptions.MissingStreetException;
import com.example.geocoder.exceptions.MissingZipOrCityStateException;
import com.example.geocoder.requests.AddressRequest;
import com.example.geocoder.responses.AddressMatchResponse;
import com.example.geocoder.responses.CensusApiResponse;
import com.example.geocoder.responses.CoordinatesResponse;
import com.example.geocoder.responses.ResultResponse;

@ExtendWith(MockitoExtension.class)
class CensusApiServiceTests {
  @Mock
  private RestClient mockClient;

  @InjectMocks
  private CensusApiService censusApiService;

  @Test
  void submitAddressSuccessfulReqeuest() {
    AddressRequest successfulRequest = new AddressRequest("20 W 34th St.", "New York", "New York", "10001");

    CoordinatesResponse coordinates = new CoordinatesResponse(-73.98524258380219, 40.74865337901453);
    AddressMatchResponse expectedAddressMatch = new AddressMatchResponse("20 W 34TH ST, NEW YORK, NY, 10118",
        coordinates);
    ResultResponse expectedResult = new ResultResponse(expectedAddressMatch);
    CensusApiResponse expectedResponse = new CensusApiResponse(expectedResult);

    RestClient.RequestHeadersUriSpec mockUriSpec = mock(RestClient.RequestHeadersUriSpec.class);
    when(mockClient.get()).thenReturn(mockUriSpec);

    RestClient.RequestHeadersSpec mockHeadersSpec = mock(RestClient.RequestHeadersSpec.class);
    when(mockUriSpec.uri("?street={street}&city={city}&state={state}&zip={zip}&benchmark=Public_AR_Current&format=json",
        successfulRequest.street(),
        successfulRequest.city(),
        successfulRequest.state(),
        successfulRequest.zip())).thenReturn(mockHeadersSpec);

    RestClient.ResponseSpec mockResponseSpec = mock(RestClient.ResponseSpec.class);
    when(mockHeadersSpec.retrieve()).thenReturn(mockResponseSpec);

    when(mockResponseSpec.body(CensusApiResponse.class)).thenReturn(expectedResponse);

    CensusApiResponse actualResponse = censusApiService.submitAddress(successfulRequest);

    assertEquals(expectedResponse, actualResponse);
  }

  @Test
  void submitAddressInvalidRequest() {
    AddressRequest invalidRequest = new AddressRequest("Invalid", "Nowhere", "Don't", "Exist");

    ResultResponse expectedResult = new ResultResponse(Collections.emptyList());
    CensusApiResponse expectedResponse = new CensusApiResponse(expectedResult);
    
    RestClient.RequestHeadersUriSpec mockUriSpec = mock(RestClient.RequestHeadersUriSpec.class);
    when(mockClient.get()).thenReturn(mockUriSpec);

    RestClient.RequestHeadersSpec mockHeadersSpec = mock(RestClient.RequestHeadersSpec.class);
    when(mockUriSpec.uri("?street={street}&city={city}&state={state}&zip={zip}&benchmark=Public_AR_Current&format=json",
        invalidRequest.street(),
        invalidRequest.city(),
        invalidRequest.state(),
        invalidRequest.zip())).thenReturn(mockHeadersSpec);

    RestClient.ResponseSpec mockResponseSpec = mock(RestClient.ResponseSpec.class);
    when(mockHeadersSpec.retrieve()).thenReturn(mockResponseSpec);

    when(mockResponseSpec.body(CensusApiResponse.class)).thenReturn(expectedResponse);

    assertThrows(InvalidAddressException.class, () -> censusApiService.submitAddress(invalidRequest));

  }

  @Test
  void validateRequestMissingStreetTest() {
    AddressRequest addressRequest = new AddressRequest("", "Anywhere", "Anywhere", "Anywhere");
    assertThrows(MissingStreetException.class, () -> censusApiService.validateRequest(addressRequest));
  }

  @Test
  void validateRequestMissingZipValidCityState(){
    AddressRequest addressRequest = new AddressRequest("123 Main St", "AnyWhere", "Anywhere", "");
    assertDoesNotThrow(() -> censusApiService.validateRequest(addressRequest));
  }

  @Test
  void validateRequestMissingCityStateValidZip(){
    AddressRequest addressRequest = new AddressRequest("123 Main St", "", "", "Anywhere");
    assertDoesNotThrow(() -> censusApiService.validateRequest(addressRequest));
  }

  @Test
  void validateRequestMissingCityStateZip(){
    AddressRequest addressRequest = new AddressRequest("123 Main St", "", "", "");
    assertThrows(MissingZipOrCityStateException.class, () -> censusApiService.validateRequest(addressRequest));
  }
}