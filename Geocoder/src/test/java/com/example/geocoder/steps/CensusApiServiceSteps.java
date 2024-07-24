package com.example.geocoder.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.geocoder.CucumberContextConfig;
import com.example.geocoder.requests.AddressRequest;
import com.example.geocoder.responses.CensusApiResponse;
import com.example.geocoder.responses.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@SpringBootTest
@AutoConfigureMockMvc
public class CensusApiServiceSteps extends CucumberContextConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private MvcResult mvcResult;
    private AddressRequest addressRequest;

    @Given("the application is running")
    public void applicationIsRunning() throws Exception {
        mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/address/health")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Given("an address request with street {string}, city {string}, state {string}, and zip {string}")
    public void invalidAddressRequest(String street, String city, String state, String zip) {
        addressRequest = new AddressRequest(street, city, state, zip);
    }

    @When("the address is submitted to the Census API")
    public void theAddressIsSubmitted() throws Exception {
        mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/address/submittedAddress/request")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(addressRequest)))
                .andReturn();
    }

    @Then("the response status should be {int}")
    public void responseStatusShouldBe(int expected) {
        assertEquals(expected, mvcResult.getResponse().getStatus());
    }

    @Then("the response should contain a latitude of {double} and longitude of {double}")
    public void responseContainsValidAddressMatch(Double latitude, Double longitude) throws Exception {
        CensusApiResponse response = mapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                CensusApiResponse.class);
        assertThat(response.result().addressMatches().get(0).coordinates().x()).isEqualTo(latitude);
        assertThat(response.result().addressMatches().get(0).coordinates().y()).isEqualTo(longitude);
    }

    @Then("the response content should be an error")
    public void responseIserror() throws Exception {
        ErrorResponse response = mapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                ErrorResponse.class);
        assertThat(response.error()).isNotEmpty();
    }

}
