package com.base;

import com.exceptions.FrameworkException;
import com.utils.LoggerUtils;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

public final class DriverFactory {

    private static final Logger LOGGER = LoggerUtils.getLogger(DriverFactory.class);

    private DriverFactory() {
        // Private constructor to prevent instantiation
    }

    public static WebDriver initDriver(String browserName) throws Throwable {
        BrowserType browserType = BrowserType.fromString(browserName);
        String gridUrl = System.getProperty("grid.url");
        WebDriver driver;
        try{
            if(gridUrl !=null && !gridUrl.trim().isEmpty()) {
                driver = createRemoteDriver(browserType, gridUrl);
                LOGGER.info("Initialized remote WebDriver for browser: " + browserType + " using grid URL: " + gridUrl);
            }
            else{
                driver = createLocalDriver(browserType);
                LOGGER.info("Initialized local WebDriver for browser: " + browserType);
            }
        }
        catch(Exception e){
            throw new FrameworkException("Failed to initialize WebDriver for browser: " + browserType, e);
        }
        configureTimeouts(driver);
        DriverManager.setDriver(driver);
        return driver;
    }

    private static void configureTimeouts(WebDriver driver) {

    }

    private static WebDriver createLocalDriver(BrowserType browserType) {
        return null;
    }

    private static WebDriver createRemoteDriver(BrowserType browserType, String gridUrl) {
        return null;
    }
}
