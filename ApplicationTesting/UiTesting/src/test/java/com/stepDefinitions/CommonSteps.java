package com.stepDefinitions;

import com.base.DriverManager;
import com.pages.LoginPage;
import com.utils.LoggerUtils;
import com.utils.TestBase;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class CommonSteps extends TestBase {

    private final LoginPage loginPage = new LoginPage(DriverManager.getDriver());
    private final TestBase base = new TestBase(DriverManager.getDriver());
    private static final Logger LOGGER = LoggerUtils.getLogger(CommonSteps.class);

    public CommonSteps(WebDriver driver) {
        super(driver);
    }

    @When("User is on the login page")
    public void user_is_on_the_login_page() {
        try{
           if(base.validateTitle("Login - Screener")){
               base.click(loginPage.loginButton);
               if(base.isElementPresent(loginPage.emailField)){
                   LOGGER.info("User is on the login page");
               } else {
                   LOGGER.error("Login page is not displayed");
               }
           }
        } catch (Exception e) {
            LOGGER.error("Error occurred while checking login page: " + e.getMessage());
        }
    }
}
