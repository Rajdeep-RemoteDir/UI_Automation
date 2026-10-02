package com.stepDefinitions;

import com.base.DriverManager;
import com.listeners.ExtentReportListener;
import com.pages.LoginPage;
import com.utils.LoggerUtils;
import com.utils.TestBase;
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
}
