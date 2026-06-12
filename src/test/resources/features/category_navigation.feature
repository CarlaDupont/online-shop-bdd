Feature: Category navigation

  Scenario: User can access categories page
    When the user opens the categories page
    Then the categories page is displayed

  Scenario: User selects a category
    Given the following products exist
      | reference | name      | category | price |
      | P001      | Java book | Books    | 39.99 |
      | P002      | JS book   | Books    | 29.99 |
      | P003      | Mouse     | Accessories | 19.99 |
    When the user selects category "Books"
    Then 2 products are returned
    And the results contain product "P001"
    And the results contain product "P002"