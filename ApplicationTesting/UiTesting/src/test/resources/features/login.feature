@login
Feature: User Login
  As a registered user
  I want to log in to the application
  So that I can access my account
  @Smoke
  Scenario: login with valid & invalid credentials
#    When User is on the login page
    Given the user logs in with username "<username>" and password "<password>"
#    Then the user should see an error message "Invalid username or password" should be displayed

#  Scenario Outline: Login attempts with various credentials
#    When User is on the login page
#    And the user logs in with username "<username>" and password "<password>"
#    Then the user should see an error message "<error_message>" should be displayed

#    Examples:
#      | username       | password           | error_message                  |
#      | ""             | "Invalid_password" | "Username cannot be empty"     |
#      | "Valid_user"   | ""                 | "Password cannot be empty"     |
#      | "Invalid_user" | "Valid_password"   | "Invalid username or password" |