package com.base;

import com.config.ConfigReader;
import com.exceptions.FrameworkException;
import com.utils.LoggerUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;

import java.time.Duration;


public final class DriverFactory {

    private static final Logger LOGGER = LoggerUtils.getLogger(DriverFactory.class);

    private DriverFactory() {
        // Private constructor to prevent instantiation
    }

    public static WebDriver initDriver() throws Throwable {
        String browserName = System.getProperty("browser", ConfigReader.get("browser"));
        boolean headless = readBooleanConfig("headless", ConfigReader.get("headless").equalsIgnoreCase("true"));
        return initDriver(browserName, headless);
    }

    public static WebDriver initDriver(String browserName,boolean headless) throws Throwable {
        BrowserType browserType = BrowserType.fromString(browserName);
        String gridUrl = System.getProperty("grid.url");
        WebDriver driver = null;
        try{
            if(gridUrl !=null && !gridUrl.trim().isEmpty()) {
                // driver = createRemoteDriver(browserType,headless, gridUrl);
                LOGGER.info("Initialized remote WebDriver for browser: " + browserType + " using grid URL: " + gridUrl);
            }
            else{
                driver = createLocalDriver(browserType,headless);
                LOGGER.info("Initialized local WebDriver for browser: " + browserType + ", headless=" + headless);
            }
        }
        catch(Exception e){
            throw new FrameworkException("Failed to initialize WebDriver for browser: " + browserType, e);
        }
        configureTimeouts(driver);
        if (!headless) {
            driver.manage().window().maximize();
        }
        DriverManager.setDriver(driver);
        return driver;
    }

    private static void configureTimeouts(WebDriver driver) {
        int implicitWait = readIntConfig("implicitWait", 2);
        int pageLoadTimeout = readIntConfig("pageLoadTimeout", 30);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
    }

    private static WebDriver createLocalDriver(BrowserType browserType,boolean headless) {
        switch(browserType){
            case CHROME:
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver(buildChromeOptions(headless));
            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                return new FirefoxDriver(buildFirefoxOptions(headless));
            case EDGE:
                WebDriverManager.edgedriver().setup();
                return new EdgeDriver(buildEdgeOptions(headless));
            case SAFARI:
                if(headless){
                    LOGGER.warn("Headless mode is not supported for Safari browser.");
                }
                return new SafariDriver();
            default:
                throw new FrameworkException("No Local driver mapping defined for : " + browserType);
        }
    }

    private static EdgeOptions buildEdgeOptions(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--disable-gpu");
        } else {
            options.addArguments("--start-maximized");
        }
        options.addArguments("--window-size=1920,1080");
        return options;
    }

    private static FirefoxOptions buildFirefoxOptions(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("--headless");
        } else {
            options.addArguments("--start-maximized");
        }
        options.addArguments("--width=1920");
        options.addArguments("--height=1080");
        return options;
    }

    private static ChromeOptions buildChromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        if(headless){
            options.addArguments("--headless=new");
            options.addArguments("--disable-gpu");
        }
        else{
            options.addArguments("--start-maximized");
        }
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");
        return options;
    }

    private static boolean readBooleanConfig(String key, boolean defaultValue) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.trim().isEmpty()) {
            return Boolean.parseBoolean(systemValue.trim());
        }

        String configValue = readConfigValue(key);
        if (configValue == null || configValue.trim().isEmpty()) {
            return defaultValue;
        }

        return Boolean.parseBoolean(configValue.trim());
    }

    private static int readIntConfig(String key, int defaultValue) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.trim().isEmpty()) {
            try {
                return Integer.parseInt(systemValue.trim());
            } catch (NumberFormatException e) {
                LOGGER.warn("Invalid integer value for config key: " + key + ". Using default: " + defaultValue);
            }
        }

        String configValue = readConfigValue(key);
        if (configValue == null || configValue.trim().isEmpty()) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(configValue.trim());
        } catch (NumberFormatException e) {
            LOGGER.warn("Invalid integer value for config key: " + key + ". Using default: " + defaultValue);
            return defaultValue;
        }
    }

    private static String readConfigValue(String key) {
        try {
            return ConfigReader.get(key);
        } catch (RuntimeException e) {
            return null;
        }
    }

    public static void quitDriver() {
        if(DriverManager.isDriverintialized()){
            try{
                DriverManager.getDriver().quit();
                LOGGER.info("WebDriver instance quit successfully for thread: " + Thread.currentThread().getName());
            } catch (Exception e) {
                LOGGER.error("Error while quitting WebDriver instance for thread: " + Thread.currentThread().getName(), e);
            } finally {
                DriverManager.unload();
            }
        }
    }

}
