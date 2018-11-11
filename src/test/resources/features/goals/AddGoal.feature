@regression
Feature: Add goals

  @countGoalsBefore
  Scenario: Add a goal
    Given the property:"name" with value:"John Doe"
    And the property:"notes" with value:"Get certification"
    And the property:"dueDate" with value:"2018-12-08T22:47:22"
    And the property:"weight" with value:22
    When I send the request to addGoal service
    Then the response code is "200"
    And there is a new goal added
    And response string field "name" is "John Doe"
    And response string field "notes" is "Get certification"
    And response int field "weight" is "22"
    And response long field "dueDate" is "1544309242000L"

  @countGoalsBefore
  Scenario Outline: All fields are optional
    Given the property:"name" with value:"John Doe"
    And the property:"notes" with value:"Get certification"
    And the property:"dueDate" with value:"2018-12-08T22:47:22"
    And the property:"weight" with value:22
    And the property:"<key>" with value:"null"
    When I send the request to addGoal service
    Then the response code is "200"
    And there is a new goal added

    Examples:
    |key|
    |name|
    |notes|
    |dueDate|
    |weight|

  Scenario: Get goals results are identical
    Given I get the goals service
    When I get the goals service again
    And both results are identical

    Scenario: generated ID is unique
      When I get the goals service
      Then all IDs are unique
