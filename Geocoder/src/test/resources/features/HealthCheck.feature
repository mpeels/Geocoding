Feature: Health check

  Scenario: An application health check is available
    When I make an application health check request
    Then I recieve a valid health check response