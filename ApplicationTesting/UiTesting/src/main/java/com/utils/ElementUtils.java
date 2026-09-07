package com.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ElementUtils {
    private final WebDriver driver;
    private final WaitUtils waitUtils;

    public ElementUtils(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
    }
    public void click(By locator) {
        waitUtils.waitForClickability(locator).click();
    }

    public void type(By locator, String text) {
        WebElement element = waitUtils.waitForElementToBeVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    public String getText(By locator) {
        return waitUtils.waitForElementToBeVisible(locator).getText();
    }

    public boolean isDisplayed(By locator) {
        return waitUtils.waitForElementToBeVisible(locator).isDisplayed();
    }
}
