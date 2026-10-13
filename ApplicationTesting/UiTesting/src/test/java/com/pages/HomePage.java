package com.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends  BasePage {
    public HomePage(WebDriver driver) {
        super(driver);
    }

    // Locators for HomePage elements can be added here

    public final By pageLogoHeaderLnk = By.xpath("//div[@class='nI-gNb-header']//img[@alt='Naukri.com']/parent::a");
    public final By jobsHeaderLnk = By.xpath("//ul[@class='nI-gNb-menus']/li//span[text()='Jobs']/ancestor::a");
    public final By companiesHeaderLnk = By.xpath("//ul[@class='nI-gNb-menus']/li//span[text()='Companies']/ancestor::a");
    public final By servicesHeaderLnk = By.xpath("//ul[@class='nI-gNb-menus']/li//span[text()='Services']/ancestor::a");
    public final By searchBoxtxtBoxHeader = By.xpath("//div[@class='nI-gNb-search-bar']//button[contains(@aria-label,'Search jobs')]");
    public final By searchBtnHeader = By.xpath("//div[@class='nI-gNb-search-bar']//button[@class='nI-gNb-sb__icon-wrapper']");
    public final By notificationHeader = By.xpath("//div[@class='nI-gNb-notif-center']/button[@aria-label='Notifications']");
    public final By loginDropdownHeader = By.xpath("//button[@class='nI-gNb-drawer__icon']");



}

