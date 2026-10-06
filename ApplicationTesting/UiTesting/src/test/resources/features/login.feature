@login
Feature: User Login
  As a registered user
  I want to log in to the application
  So that I can access my account
  @Smoke
  Scenario: login with valid & invalid credentials
    When User is on the login page
    Given the user logs in with Valid username and password
    Then User logouts from the application