Feature: Health check

  @smoke
  Scenario: Check if application is up
    When I ping the host
    Then application is up

  @smoke
  Scenario: get all goals - Service is up
    When I get the goals service
    Then the response code is "200"

  @smoke
  Scenario: get hello - Service is Up
    When I get the hello service
    Then the response code is "200"
