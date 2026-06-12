Feature: Product search

  Scenario: User can access the search bar
    When the user opens the search bar
    Then the search bar is displayed

  Scenario: User searches products by keyword
    Given the following products exist
      | reference | name          | category    | price |
      | P001      | Gaming mouse  | Accessories | 49.99 |
      | P002      | Office mouse  | Accessories | 19.99 |
      | P003      | Java book     | Books       | 39.99 |
    When the user searches products with keyword "mouse"
    Then 2 products are returned
    And the results contain product "P001"
    And the results contain product "P002"

  Scenario: User searches products by maximum price
    Given the following products exist
      | reference | name          | category    | price |
      | P001      | Gaming mouse  | Accessories | 49.99 |
      | P002      | Office mouse  | Accessories | 19.99 |
      | P003      | Mechanical keyboard | Accessories | 129.99 |
    When the user searches products with maximum price 50.00
    Then 2 products are returned
    And the results contain product "P001"
    And the results contain product "P002"