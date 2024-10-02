Feature: Ride service test
  Scenario: Create a new Ride
    Given Ride request with driverId 1, passengerId 2, routeStart "Point A", and routeEnd "Point B"
    When Ride is created
    Then the ride should have status "CREATED"
    And the ride should have driverId 1 and passengerId 2

  Scenario: Change the status of a ride
    Given an existing ride with id 1 and status "CREATED"
    When the ride status is changed to "FINISHED"
    Then the ride should have status "FINISHED"

  Scenario: Get Ride by id when Ride exist
    Given an existing ride with id 1
    When the id 1 is passed to the findById method
    Then The response should contain ride with id 1

