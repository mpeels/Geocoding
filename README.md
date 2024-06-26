# Geocoding
Spring Boot Microservice to connect with the U.S. Census API

Access through port 8081

CensusService.java:

    Construction:
        When an instances of CensusService is created it will intalize with an instances of RestClient.

    submittedAddress(Address address)
        Calling the submittedAddress function takes an address object as an input to create a uri path
        that the RestClient uses to submit a request to the US Census API. The api's response is then 
        mapped to a Response object.
    
    Updates:
        Transitioned to RestClient instead of WebClient to produce more readable and simple code.

GeocoderController.java:

    Run Process: Start project from terminal with './gradlw bootRun' while in directory or run from GecoderApplication

    Initializes the Census Service and takes the users address input before making a call to the US Census API
    and returning a response. 


Resources:
- https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#_creating_a_restclient
- https://spring.io/blog/2023/07/13/new-in-spring-6-1-restclient
- https://www.baeldung.com/spring-response-entity
