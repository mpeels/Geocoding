package com.example.geocoder.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;


import com.example.geocoder.CucumberContextConfig;
import com.example.geocoder.exceptions.InvalidAddressException;
import com.example.geocoder.exceptions.MissingStreetException;
import com.example.geocoder.exceptions.MissingZipAndCityStateException;
import com.example.geocoder.requests.AddressRequest;
import com.example.geocoder.responses.AddressMatchResponse;
import com.example.geocoder.responses.CensusApiResponse;
import com.example.geocoder.responses.CoordinatesResponse;
import com.example.geocoder.responses.ResultResponse;
import com.example.geocoder.services.CensusApiService;

import com.fasterxml.jackson.databind.ObjectMapper;




@SpringBootTest
@AutoConfigureMockMvc
public class CensusApiServiceSteps extends CucumberContextConfig {

    @Autowired
    private MockMvc mockMvc;

    private MvcResult mvcResult;

    @Autowired
    private CensusApiService censusApiService;

    private AddressRequest addressRequest;
    private CensusApiResponse response;
    private Exception exception;

    
    @Given("the application is running")
    public void applicationIsRunning() throws Exception {
        AddressRequest address = new AddressRequest("1600 Pennsylvania Ave", "Washington", "DC", "20500"); 
        mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/address/health")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @When("I submit a POST request to {string} with body {string}")
    public void submitAPostRequestWithBody(String url, String body) throws Exception { 
        mvcResult = mockMvc.perform(MockMvcRequestBuilders
            .post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andExpect(status().isOk())
            .andReturn();
    }
    
    @Then("the response status should be {int}")
    public void responseStatusShouldBe200(int status) throws Exception {
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(status);
    }

    @And("the response should contain a valid address match")
    public void responseContainsValidAddressMatch() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBody = mvcResult.getResponse().getContentAsString();
        CensusApiResponse mockApiResponse = objectMapper.readValue(responseBody, CensusApiResponse.class);
        
        CoordinatesResponse expectedCoordinatesResponse = new CoordinatesResponse(-77.03654395730787, 38.89869091865549);
        AddressMatchResponse expectedMatchResponse = new AddressMatchResponse("1600 PENNSYLVANIA AVE NW, WASHINGTON, DC, 20500", expectedCoordinatesResponse);
        ResultResponse expectedResult = new ResultResponse(expectedMatchResponse);
        CensusApiResponse expectedResponse = new CensusApiResponse(expectedResult);

        assertThat(mockApiResponse).isEqualToComparingFieldByFieldRecursively(expectedResponse);
    }
 

    @Given("an address request with missing street, city {string}, state {string}, and zip {string}")
    public void missingStreetAddressRequest(String city, String state, String zip){
        addressRequest = new AddressRequest("", city, state, zip);
    }

    @Given("an address request with street {string}, missing city, missing state, and missing zip")
    public void missingCityStateZipAddressRequest(String street){
        addressRequest = new AddressRequest(street, "", "", "");
    }

    @Given("an address request with street {string}, city {string}, state {string}, and zip {string}")
    public void invalidAddressRequest(String street, String city, String state, String zip){
        addressRequest = new AddressRequest(street, city, state, zip);
    }

    @When("the address is submitted to the Census API")
    public void theAddressIsSubmitted(){
        try{
            response = censusApiService.submitAddress(addressRequest);
        }catch(Exception e){
            exception = e; 
        }
    }

    @Then("a MissingStreetException should be thrown")
    public void throwMissingStreetException(){
        assertEquals(MissingStreetException.class, exception.getClass());
    }

    @Then("a MissingZipAndCityStateException should be thrown")
    public void throwMissingZipAndCityStateException(){
        assertEquals(MissingZipAndCityStateException.class, exception.getClass());
    }

    @Then("an InvalidAddressException should be thrown")
    public void throwInvalidAddressException(){
        assertEquals(InvalidAddressException.class, exception.getClass());
    }
}
