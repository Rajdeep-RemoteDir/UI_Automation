package com.pages;

import com.base.DriverManager;
import com.utils.ElementUtils;
import com.utils.TestBase;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class BasePage extends TestBase {
    public BasePage() {
        this(DriverManager.getDriver());
    }
    protected WebDriver driver;
    protected ElementUtils elementUtils;

    public BasePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.elementUtils = new ElementUtils(driver);
        PageFactory.initElements(driver, this);
    }
}
