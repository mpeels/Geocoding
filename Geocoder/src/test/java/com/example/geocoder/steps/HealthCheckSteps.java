package com.example.geocoder.steps;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@AutoConfigureMockMvc
public class HealthCheckSteps {
  @Autowired
  private MockMvc mockMvc;

  private MvcResult mvcResult;

  @When("I make an application health check request")
  public void sendHealthCheckRequest() throws Exception {
    mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .get("/health")
        .contentType(MediaType.APPLICATION_JSON))
        .andReturn();
  }

  @Then("I recieve a valid health check response")
  public void recieveHealthCheckResponse() throws UnsupportedEncodingException {
    assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
    assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("Yes, this application is still alive!");
  }

}
