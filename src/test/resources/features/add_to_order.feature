Feature: Add product to order

  Scenario: User adds a product to an order
    Given order "O001" exists
    And product "P001" named "Keyboard" exists in category "Accessories" with price 79.99
    When the user adds product "P001" to order "O001"
    Then product addition is confirmed
    And order "O001" contains product "P001" with quantity 1

  Scenario: Product quantity increases when product is already in order
    Given order "O002" exists
    And product "P001" named "Keyboard" exists in category "Accessories" with price 79.99
    And order "O002" already contains product "P001" with quantity 1
    When the user adds product "P001" to order "O002"
    Then product addition is confirmed
    And order "O002" contains product "P001" with quantity 2

  Scenario: Add product fails when order does not exist
    Given order "UNKNOWN" does not exist
    And product "P001" named "Keyboard" exists in category "Accessories" with price 79.99
    When the user adds product "P001" to order "UNKNOWN"
    Then an error is returned
    And the error message contains "Order not found"