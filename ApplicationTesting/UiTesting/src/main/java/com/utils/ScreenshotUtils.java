package com.utils;

import com.base.DriverManager;
import com.constants.FrameworkConstants;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ScreenshotUtils extends TestBase {

    private static final TestBase base = new TestBase(DriverManager.getDriver());

    public ScreenshotUtils(WebDriver driver) {
        super(driver);
    }

    public static String capture(WebDriver driver, String testName){
        try{
            File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            String timestamp = base.getCurrentTimeStamp("yyyyMMdd_HHmmss");
            String fileName = testName+"_" + timestamp + ".png";
            Files.createDirectories(Paths.get(FrameworkConstants.SCREENSHOT_DIR));
            String destPath = FrameworkConstants.SCREENSHOT_DIR + fileName;
            Files.copy(src.toPath(), Paths.get(destPath));
            return destPath;
        }
        catch(Exception e){
            throw new RuntimeException("Failed to capture screenshot for test: " + testName, e);
        }
    }
}
