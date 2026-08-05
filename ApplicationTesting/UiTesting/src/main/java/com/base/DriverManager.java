package com.base;

import com.utils.LoggerUtils;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

public final class DriverManager {

    private static final Logger LOGGER = LoggerUtils.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();

    private DriverManager() {
        // Private constructor to prevent instantiation
    }
    public static WebDriver getDriver(){
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if(driver == null){
            throw new IllegalStateException("WebDriver instance is not initialized for thread: " + Thread.currentThread().getName());
        }
        return driver;
    }

    public static void setDriver(WebDriver driver){
        DRIVER_THREAD_LOCAL.set(driver);
        LOGGER.debug("Driver set for thread: "+Thread.currentThread().getName());
    }

    public static boolean isDriverintialized(){
        return DRIVER_THREAD_LOCAL.get() != null;
    }

    public static void unload(){
        DRIVER_THREAD_LOCAL.remove();
        LOGGER.debug("Driver removed for thread: "+Thread.currentThread().getName());
    }
}
