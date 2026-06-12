Feature: Order checkout

  Scenario: User can access checkout form
    When the user opens the checkout form
    Then the checkout form is displayed

  Scenario: User validates an order successfully
    Given order "O006" exists
    When the user validates order "O006"
    Then order validation is confirmed
    And the confirmation message contains "Order confirmed"

  Scenario: Order validation fails when order does not exist
    Given order "UNKNOWN" does not exist
    When the user validates order "UNKNOWN"
    Then an error is returned
    And the error message contains "Order not found"