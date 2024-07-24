package com.example.geocoder;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@AutoConfigureMockMvc
@SpringBootTest
public class CucumberContextConfig {
}
