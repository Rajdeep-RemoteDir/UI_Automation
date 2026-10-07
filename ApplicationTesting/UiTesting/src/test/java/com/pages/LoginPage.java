package com.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    public LoginPage(WebDriver driver){
        super(driver);
    }

    public final By loginButton = By.xpath("//a[@id='login_Layer' and text()='Login']");
    public final By registerButton = By.xpath("//a[@id='register_Layer' and text()='Register']");

    public final By emailField = By.xpath("//div[@class='login-layer']//input[contains(@placeholder,'Email ID')]");
    public final By passwordField = By.xpath("//div[@class='login-layer']//input[contains(@placeholder,'Enter your password')]");
    public final By loginWithCredentials = By.xpath("//button[@type='submit' and text()='Login']");

    public final By loginDropdown = By.xpath("//button[@class='nI-gNb-drawer__icon']");
    public final By logoutButton = By.xpath("//a[@class='nI-gNb-list-cta' and @title='Logout' and contains(.,'Logout')]");


}
