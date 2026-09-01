package com.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.stepdefinitions",
        plugin = {"pretty", "html:test-output/cucumber-reports.html","com.listener.ExtentReportListener"},
        monochrome = true,
        tags = "@RegressionTest"
)
public class RegressionTestRunner extends AbstractTestNGCucumberTests {
}
