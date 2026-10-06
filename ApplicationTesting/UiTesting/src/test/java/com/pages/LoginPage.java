package com.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    public LoginPage(WebDriver driver){
        super(driver);
    }

    public final By loginButton = By.xpath("//a[contains(@href,'login')]");
    public final By getFreeAccountButton = By.xpath("//a[contains(text(),'free account')]");

    public final By emailField = By.xpath("//div[@class='form-field']/input[@name='username']");
    public final By passwordField = By.xpath("//div[@class='form-field']/input[@name='password']");
    public final By loginWithCredentials = By.xpath("//section[@class='auth-card']//form//button[text()='Log in']");
    public final By forgetPasswordLink = By.xpath("//a[contains(text(),'Lost password')]");

    public final By loginDropdown = By.xpath("//div[@id='account-dropdown']/button/i[@class='icon-down']");
    public final By logoutButton = By.xpath("//div[@id='nav-user-menu']//button[contains(.,'Logout')]");


}
