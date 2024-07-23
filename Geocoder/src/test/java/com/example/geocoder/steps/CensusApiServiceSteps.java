package com.example.geocoder.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.geocoder.CucumberContextConfig;
import com.example.geocoder.exceptions.InvalidAddressException;
import com.example.geocoder.exceptions.MissingStreetException;
import com.example.geocoder.exceptions.MissingZipAndCityStateException;
import com.example.geocoder.requests.AddressRequest;
import com.example.geocoder.responses.CensusApiResponse;
import com.example.geocoder.services.CensusApiService;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@SpringBootTest
public class CensusApiServiceSteps extends CucumberContextConfig {

    @Autowired
    private CensusApiService censusApiService;

    private AddressRequest addressRequest;
    private CensusApiResponse response;
    private Exception exception;

    @Given("a valid address request with street {string}, city {string}, state {string}, and zip {string}")
    public void validAddressRequest(String street, String city, String state, String zip){
        addressRequest = new AddressRequest(street, city, state, zip);
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

    @Then("the response should contain a valid address match")
    public void theResponseShouldContainValidAddressMatches(){
        assertNotNull(response);
        assertFalse(response.result().addressMatches().isEmpty());
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
