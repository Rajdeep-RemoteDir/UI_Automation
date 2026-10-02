package com.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.stepDefinitions",
        plugin = {"pretty", "html:test-output/cucumber-reports.html","com.listeners.ExtentReportListener"},
        monochrome = true,
        tags = "@Test"
)
public class TestRunner extends AbstractTestNGCucumberTests {
}
