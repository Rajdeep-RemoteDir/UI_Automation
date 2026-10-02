package com.stepDefinitions;

import com.base.DriverFactory;
import com.base.DriverManager;
import com.config.ConfigReader;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class Hooks {

    @Before
    public void setUp() {
        try {
            DriverFactory.initDriver();
            DriverManager.getDriver().manage().deleteAllCookies();
            DriverManager.getDriver().get(ConfigReader.get("baseUrl"));
        } catch (Throwable e) {
            throw new RuntimeException("Failed to initialize browser for scenario", e);
        }
    }

    @After
    public void tearDown(Scenario scenario){
        if(scenario.isFailed() && DriverManager.isDriverintialized()){
            byte[] screenshot = ((TakesScreenshot)DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName());
        }
        DriverFactory.quitDriver();
    }
}
