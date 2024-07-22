package com.example.geocoder.steps;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.geocoder.SpringIntegrationTest;
import com.example.geocoder.requests.AddressRequest;
import com.example.geocoder.responses.CensusApiResponse;
import com.example.geocoder.services.CensusApiService;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@SpringBootTest
public class CensusApiServiceSteps extends SpringIntegrationTest {

    @Autowired
    private CensusApiService censusApiService;

    private AddressRequest addressRequest;

    private CensusApiResponse response;

    @Given("a valid address request with street {string}, city {string}, state {string}, and zip {string}")
    public void validAddressRequest(String street, String city, String state, String zip){
        addressRequest = new AddressRequest(street, city, state, zip);
        // assertThat(street).isNotEmpty();
    }

    @When("the address is submitted to the Census API")
    public void theAddressIsSubmitted(){
        response = censusApiService.submitAddress(addressRequest);
    }

    @Then("the response should contain a valid address match")
    public void theResponseShouldContainValidAddressMatches(){
        assertNotNull(response);
    }
}
