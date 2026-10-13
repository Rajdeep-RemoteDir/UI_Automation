package com.stepDefinitions;

import com.base.DriverManager;
import com.pages.HomePage;
import com.utils.LoggerUtils;
import com.utils.TestBase;
import org.apache.logging.log4j.Logger;

public class HomepageSteps extends TestBase {

    private static final Logger LOGGER = LoggerUtils.getLogger(HomepageSteps.class);

    private HomePage getHomePage() {
        return new HomePage(DriverManager.getDriver());
    }

    private TestBase getBase() {
        return new TestBase(DriverManager.getDriver());
    }
    TestBase base = getBase();
    HomePage homePage = getHomePage();



}
