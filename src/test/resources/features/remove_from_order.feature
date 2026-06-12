Feature: Remove product from order

  Scenario: Product quantity decreases when quantity is greater than one
    Given order "O003" exists
    And product "P001" named "Keyboard" exists in category "Accessories" with price 79.99
    And order "O003" already contains product "P001" with quantity 2
    When the user removes product "P001" from order "O003"
    Then product removal is confirmed
    And order "O003" contains product "P001" with quantity 1

  Scenario: Product is removed when quantity is one
    Given order "O004" exists
    And product "P001" named "Keyboard" exists in category "Accessories" with price 79.99
    And order "O004" already contains product "P001" with quantity 1
    When the user removes product "P001" from order "O004"
    Then product removal is confirmed
    And order "O004" does not contain product "P001"

  Scenario: Remove product fails when product is not in order
    Given order "O005" exists
    And product "P001" named "Keyboard" exists in category "Accessories" with price 79.99
    When the user removes product "P001" from order "O005"
    Then an error is returned
    And the error message contains "Product not found in order"

  Scenario: Remove product fails when order does not exist
    Given order "UNKNOWN" does not exist
    When the user removes product "P001" from order "UNKNOWN"
    Then an error is returned
    And the error message contains "Order not found"