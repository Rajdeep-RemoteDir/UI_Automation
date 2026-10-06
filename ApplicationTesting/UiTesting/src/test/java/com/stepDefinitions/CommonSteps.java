package com.stepDefinitions;

import com.base.DriverManager;
import com.config.EnvironmentConfig;
import com.listeners.ExtentReportListener;
import com.pages.LoginPage;
import com.utils.LoggerUtils;
import com.utils.TestBase;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.Logger;

public class CommonSteps {

    private static final Logger LOGGER = LoggerUtils.getLogger(CommonSteps.class);

    private LoginPage getLoginPage() {
        return new LoginPage(DriverManager.getDriver());
    }

    private TestBase getBase() {
        return new TestBase(DriverManager.getDriver());
    }
    TestBase base = getBase();
    LoginPage loginPage = getLoginPage();

    @When("User is on the login page")
    public void user_is_on_the_login_page() {
        try {
           TestBase base = getBase();
           LoginPage loginPage = getLoginPage();
           if (base.validateTitle("Indian stocks")) {
               ExtentReportListener.logInfo("Title validated successfully. User is on expected login page.");
               base.click(loginPage.loginButton);
               if (base.isElementPresent(loginPage.emailField)) {
                   LOGGER.info("User is on the login page");
                   ExtentReportListener.logInfo("User is on the login page");
               } else {
                   String message = "Login page is not displayed";
                   LOGGER.error(message);
                   ExtentReportListener.logError(message);
                   throw new AssertionError(message);
               }
           } else {
               String currentTitle = base.getTitle();
               String message = "User is not on the expected page. Current title: " + currentTitle;
               LOGGER.error(message);
               ExtentReportListener.logError(message);
           }
        } catch (AssertionError e) {
           throw e;
        } catch (Exception e) {
           String message = "Error occurred while checking login page: " + e.getMessage();
           LOGGER.error(message);
           ExtentReportListener.logError(message);
           throw new RuntimeException(message, e);
        }
    }

    @Given("the user logs in with Valid username and password")
    public void the_user_logs_in_with_valid_username_and_password(){
        try {

            String userName = EnvironmentConfig.readData("qa", "email");
            String password = EnvironmentConfig.readData("qa", "password");
            base.type(loginPage.emailField, base.decrypt(userName) );
            base.type(loginPage.passwordField, base.decrypt(password));
            base.click(loginPage.loginWithCredentials);
            base.click(loginPage.loginDropdown);
            if(base.isElementPresent(loginPage.logoutButton)){
                LOGGER.info("User able to login successfully");
                ExtentReportListener.logInfo("User able to login successfully");
            } else {
                String message = "Login failed. Logout button not found.";
                LOGGER.error(message);
                ExtentReportListener.logError(message);
                throw new AssertionError(message);
            }
        } catch (Exception e) {
            String message = "Error occurred while logging in: " + e.getMessage();
            LOGGER.error(message);
            ExtentReportListener.logError(message);
            throw new RuntimeException(message, e);
        }
    }

    @Then("User logouts from the application")
    public void user_logouts_from_the_application() {
        try{
            if(base.isElementPresent(loginPage.logoutButton)){
                base.click(loginPage.logoutButton);
                LOGGER.info("User logged out successfully");
                ExtentReportListener.logInfo("User logged out successfully");
            }
            else if(base.isElementPresent(loginPage.loginDropdown)){
                base.click(loginPage.loginDropdown);
                if(base.isElementPresent(loginPage.logoutButton)){
                    base.click(loginPage.logoutButton);
                    LOGGER.info("User logged out successfully");
                    ExtentReportListener.logInfo("User logged out successfully");
                } else {
                    String message = "Logout button not found after clicking dropdown. User may not be logged in.";
                    LOGGER.error(message);
                    ExtentReportListener.logError(message);
                    throw new AssertionError(message);
                }
            }
            else {
                String message = "Logout button not found. User may not be logged in.";
                LOGGER.error(message);
                ExtentReportListener.logError(message);
                throw new AssertionError(message);
            }
        }
        catch (Exception e) {
            String message = "Error occurred while logging out: " + e.getMessage();
            LOGGER.error(message);
            ExtentReportListener.logError(message);
            throw new RuntimeException(message, e);
        }
    }

}
