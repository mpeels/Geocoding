Feature: Census API Service

    Scenario: Submitting a valid address request
        Given an address request with street "1600 Pennsylvania Ave NW", city "Washington", state "DC", and zip "20500"
        When the address is submitted to the Census API
        Then the response status should be 200
        And the response should contain a latitude of -77.03654395730787 and longitude of 38.89869091865549

    Scenario: Submitting an address with a missing street
        Given an address request with street "", city "Washington", state "DC", and zip "20500"
        When the address is submitted to the Census API
        Then the response status should be 400
        And the response content should be an error

    Scenario: Submitting an address with missing zip and city/state
        Given an address request with street "1600 Pennsylvania Ave NW", city "", state "", and zip ""
        When the address is submitted to the Census API
        Then the response status should be 400
        And the response content should be an error

    Scenario: Submitting an address with no matches
        Given an address request with street "Invalid Street", city "Invalid city", state " Invalid State", and zip "Fake Zip"
        When the address is submitted to the Census API
        Then the response status should be 400
        And the response content should be an error