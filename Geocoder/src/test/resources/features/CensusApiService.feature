Feature: Census API Service

    Scenario: Submitting a valid address
        Given a valid address request with street "1600 Pennsylvania Ave NW", city "Washington", state "DC", and zip "20500"
        When the address is submitted to the Census API
        Then the response should contain a valid address match