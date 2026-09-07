package com.stepDefinitions;

import com.base.DriverFactory;
import com.base.DriverManager;
import com.config.ConfigReader;
import com.utils.TestBase;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Hooks extends TestBase {
    private final TestBase base = new TestBase(DriverManager.getDriver());


    public Hooks(WebDriver driver) {
        super(driver);
    }

    @Before
    public void setUp() {
        try {
            DriverFactory.initDriver();
            DriverManager.getDriver().get(ConfigReader.get("baseUrl"));
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable e) {
            throw new RuntimeException(e);
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
