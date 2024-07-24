Feature: Census API Service

    Scenario: Submit a POST request to /api/address with a valid address
        Given the application is running
        When I submit a POST request to "/api/address/submittedAddress/request" with body "{\"street\": \"1600 Pennsylvania Ave NW\",\"city\": \"Washington\",\"state\": \"DC\",\"zip\": \"20500\"}"
        Then the response status should be 200
        And the response should contain a valid address match

    Scenario: Submitting an address with a missing street
        Given an address request with missing street, city "Washington", state "DC", and zip "20500"
        When the address is submitted to the Census API
        Then a MissingStreetException should be thrown
    
    Scenario: Submitting an address with missing zip and city/state
        Given an address request with street "1600 Pennsylvania Ave NW", missing city, missing state, and missing zip
        When the address is submitted to the Census API
        Then a MissingZipAndCityStateException should be thrown
    
    Scenario: Submitting an address with no matches
        Given an address request with street "Invalid Street", city "Invalid city", state " Invalid State", and zip "Fake Zip"
        When the address is submitted to the Census API
        Then an InvalidAddressException should be thrown