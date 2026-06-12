Feature: Account creation

  Scenario: User can access the registration form
    When the user opens the registration form
    Then the registration form is displayed

  Scenario: User creates an account successfully
    Given no account exists with username "carla"
    When the user creates an account with email "carla@mail.com", username "carla" and password "secret"
    Then account creation is confirmed
    And the confirmation message contains "carla"
    And the user account is saved

  Scenario: Account creation fails when username already exists
    Given an account already exists with username "carla"
    When the user creates an account with email "carla@mail.com", username "carla" and password "secret"
    Then an error is returned
    And the error message contains "Account already exists"
    And the user account is not saved