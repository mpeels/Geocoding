package com.example.geocoder.censusservicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import com.example.geocoder.request.AddressRequest;
import com.example.geocoder.responses.AddressMatchResponse;
import com.example.geocoder.responses.CensusApiResponse;
import com.example.geocoder.responses.CoordinatesResponse;
import com.example.geocoder.responses.ResultResponse;
import com.example.geocoder.services.CensusApiService;

@ExtendWith(MockitoExtension.class)
public class CensusServiceTests {
  @Mock
  private RestClient mockClient;

  @InjectMocks
  private CensusApiService censusApiService;

  @Test
  void submitAddressInvalidRequest() {
    AddressRequest invalidRequest = new AddressRequest("Invalid", "Nowhere", "Don't", "Exist");

    AddressMatchResponse expectedAddressMatch = new AddressMatchResponse("Address Not Found");
    ResultResponse expectedResult = new ResultResponse(expectedAddressMatch);
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

    when(mockResponseSpec.onStatus(any(), any())).thenReturn(mockResponseSpec);

    when(mockResponseSpec.body(CensusApiResponse.class)).thenReturn(expectedResponse);

    CensusApiResponse actualResponse = censusApiService.submitAddress(invalidRequest);

    assertEquals(expectedResponse, actualResponse);
  }

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

    when(mockResponseSpec.onStatus(any(), any())).thenReturn(mockResponseSpec);

    when(mockResponseSpec.body(CensusApiResponse.class)).thenReturn(expectedResponse);

    CensusApiResponse actualResponse = censusApiService.submitAddress(successfulRequest);

    assertEquals(expectedResponse, actualResponse);
  }

  @Test
  void missingParamRequestTest() {
    AddressRequest addressRequest = new AddressRequest("", "New York", "New York", "10001");
    AddressMatchResponse expectedAddressMatch = new AddressMatchResponse(
        "400 BAD_REQUEST: Street address cannot be empty and cannot exceed 100 characters, Specify House number and Street name along with City and State and/or ZIP Code");
    ResultResponse expectedResult = new ResultResponse(expectedAddressMatch);
    CensusApiResponse expectedResponse = new CensusApiResponse(expectedResult);

    RestClient.RequestHeadersUriSpec mockUriSpec = mock(RestClient.RequestHeadersUriSpec.class);
    when(mockClient.get()).thenReturn(mockUriSpec);

    RestClient.RequestHeadersSpec mockHeadersSpec = mock(RestClient.RequestHeadersUriSpec.class);
    when(mockUriSpec.uri("?street={street}&city={city}&state={state}&zip={zip}&benchmark=Public_AR_Current&format=json",
        addressRequest.street(),
        addressRequest.city(),
        addressRequest.state(),
        addressRequest.zip())).thenReturn(mockHeadersSpec);

    RestClient.ResponseSpec mockResponseSpec = mock(RestClient.ResponseSpec.class);
    when(mockHeadersSpec.retrieve()).thenReturn(mockResponseSpec);

    when(mockResponseSpec.onStatus(any(), any())).thenReturn(mockResponseSpec);

    when(mockResponseSpec.body(CensusApiResponse.class)).thenReturn(expectedResponse);

    CensusApiResponse actualResponse = censusApiService.submitAddress(addressRequest);

    assertEquals(expectedResponse, actualResponse);
  }
}
