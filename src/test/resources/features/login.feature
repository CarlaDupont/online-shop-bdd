Feature: User login

  Scenario: User can access the login form
    When the user opens the login form
    Then the login form is displayed

  Scenario: User logs in successfully
    Given a user exists with username "carla" and password "secret"
    When the user logs in with username "carla" and password "secret"
    Then an error is returned
    And the user is redirected to the home page

  Scenario: Login fails with wrong password
    Given a user exists with username "carla" and password "secret"
    When the user logs in with username "carla" and password "wrong"
    Then an error is returned
    And the error message contains "Invalid credentials"