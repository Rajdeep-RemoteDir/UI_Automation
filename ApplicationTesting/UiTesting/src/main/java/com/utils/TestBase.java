package com.utils;

import com.base.DriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

public class TestBase {

    private static final Logger LOGGER = LoggerUtils.getLogger(TestBase.class);
    private static final int DEFAULT_WAIT_SECONDS = 10;
    private final WebDriver driver;

    public TestBase(WebDriver driver) {
        this.driver = driver;
    }

    protected WebDriver driver(){
        return DriverManager.getDriver();
    }

    private WebDriverWait defaultWait(){
        return new WebDriverWait(driver(), Duration.ofSeconds(DEFAULT_WAIT_SECONDS));
    }

    //-------------- Element actions -----------------

    // Type text into an element with logging
    public void type(By Locator, String text){
        try{
            WebElement element = driver().findElement(Locator);
            ((JavascriptExecutor) driver()).executeScript("arguments[0].value = arguments[1];", element, text);
            if(isElementPresent(Locator)){
                element.clear();
                element.sendKeys(text);
                LOGGER.info("Typed text '" + text + "' into element: " + Locator);
            } else {
                LOGGER.warn("Element not present after typing text: {}", Locator.toString());
            }
        }
        catch(Exception e){
            LOGGER.error("Error typing text: " + e.getMessage());
            throw new RuntimeException("Failed to type text into element: " + Locator, e);
        }
    }

    // Validate Title of the page
    public boolean validateTitle(String expectedTitle) {
        try {
            String actualTitle = driver().getTitle();
            if (actualTitle.equals(expectedTitle)) {
                LOGGER.info("Page title validation passed. Expected: '{}', Actual: '{}'", expectedTitle, actualTitle);
                return true;
            } else {
                LOGGER.warn("Page title validation failed. Expected: '{}', Actual: '{}'", expectedTitle, actualTitle);
                return false;
            }
        } catch (Exception e) {
            LOGGER.error("Error validating page title: {}", e.getMessage());
            throw new RuntimeException("Failed to validate page title", e);
        }
    }

    // Check if an element is present with logging
    public boolean isElementPresent(By locator) {
        WebElement element = driver().findElement(locator);
        waitForElementVisible(locator, DEFAULT_WAIT_SECONDS);
        try {
            if (element != null) {
                LOGGER.info("Element is present: {}", locator.toString());
                return true;
            }
            else {
                LOGGER.warn("Element is not present: {}" ,locator.toString());
                return false;
            }
        } catch (Exception e) {
            LOGGER.error("Error checking element presence: {}", e.getMessage());
            return false;
        }
    }

    // Explite wait for element visibility with logging
    public WebElement waitForElementVisible(By locator, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver(), Duration.ofSeconds(timeoutSeconds));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

            LOGGER.info("Element became visible: {}", locator.toString());
            return element;
        } catch (TimeoutException e) {
            LOGGER.warn("Element not visible within {} seconds: {}", timeoutSeconds, locator.toString());
            return null;
        } catch (Exception e) {
            LOGGER.error("Error waiting for element visibility {}: {}", locator.toString(), e.getMessage());
            throw new RuntimeException("Failed waiting for element: " + locator, e);
        }
    }

    // get attribute value of an element with logging
    public String getAttribute(By Locator, String attributeName) {
        try {
            WebElement element = driver().findElement(Locator);
            String attributeValue = element.getAttribute(attributeName);
            LOGGER.info("Retrieved attribute '{}' with value '{}' from element: {}", attributeName, attributeValue, Locator.toString());
            return attributeValue;
        } catch (NoSuchElementException e) {
            LOGGER.warn("Element not found for getting attribute: {}", Locator.toString());
            return null;
        } catch (Exception e) {
            LOGGER.error("Error retrieving attribute '{}' from element {}: {}", attributeName, Locator.toString(), e.getMessage());
            throw new RuntimeException("Failed to get attribute from element: " + Locator, e);
        }
    }

    // Normal click on an element with logging
    public void click(By locator) {
        try {
            WebElement element = waitForElementVisible(locator, DEFAULT_WAIT_SECONDS);
            if (element != null) {
                element.click();
                LOGGER.info("Clicked on element: {}", locator.toString());
            } else {
                LOGGER.warn("Element not clickable (not visible): {}", locator.toString());
            }
        } catch (Exception e) {
            LOGGER.error("Error clicking on element {}: {}", locator.toString(), e.getMessage());
            throw new RuntimeException("Failed to click on element: " + locator, e);
        }
    }

    // Click on an element using JavaScript with logging
    public void clickUsingJS(By locator) {
        try {
            WebElement element = waitForElementVisible(locator, DEFAULT_WAIT_SECONDS);
            if (element != null) {
                ((JavascriptExecutor) driver()).executeScript("arguments[0].click();", element);
                LOGGER.info("Clicked on element using JS: {}", locator.toString());
            } else {
                LOGGER.warn("Element not clickable using JS (not visible): {}", locator.toString());
            }
        } catch (Exception e) {
            LOGGER.error("Error clicking on element using JS {}: {}", locator.toString(), e.getMessage());
            throw new RuntimeException("Failed to click on element using JS: " + locator, e);
        }
    }

    // Get all drop-down options of a select element with logging
    public List<WebElement> getDropDownOptions(By locator) {
        try {
            WebElement selectElement = waitForElementVisible(locator, DEFAULT_WAIT_SECONDS);
            if (selectElement != null) {
                Select select = new Select(selectElement);
                List<WebElement> options = select.getOptions();
                LOGGER.info("Retrieved {} options from drop-down element: {}", options.size(), locator.toString());
                return options;
            } else {
                LOGGER.warn("Drop-down element not found or not visible: {}", locator.toString());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            LOGGER.error("Error retrieving drop-down options from {}: {}", locator.toString(), e.getMessage());
            throw new RuntimeException("Failed to get drop-down options from element: " + locator, e);
        }
    }

    // select drop-down option by visible text with logging
    public void selectDropDownOptionByVisibleText(By locator, String visibleText) {
        try {
            WebElement selectElement = waitForElementVisible(locator, DEFAULT_WAIT_SECONDS);
            if (selectElement != null) {
                Select select = new Select(selectElement);
                select.selectByVisibleText(visibleText);
                LOGGER.info("Selected option '{}' from drop-down element: {}", visibleText, locator.toString());
            } else {
                LOGGER.warn("Drop-down element not found or not visible for selecting option: {}", locator.toString());
            }
        } catch (Exception e) {
            LOGGER.error("Error selecting option '{}' from drop-down {}: {}", visibleText, locator.toString(), e.getMessage());
            throw new RuntimeException("Failed to select option from drop-down element: " + locator, e);
        }
    }

    // Check if a checkbox is checked with logging
    public boolean isCheckBoxChecked(By locator) {
        try {
            WebElement checkbox = driver().findElement(locator);
            boolean isChecked = checkbox.isSelected();

            if (isChecked) {
                LOGGER.info("Checkbox is checked: {}", locator.toString());
            } else {
                LOGGER.info("Checkbox is NOT checked: {}", locator.toString());
            }

            return isChecked;
        } catch (Exception e) {
            LOGGER.error("Error checking checkbox state for {}: {}", locator.toString(), e.getMessage());
            throw new RuntimeException("Failed to check checkbox state: " + locator, e);
        }
    }

    // Take a screenshot with logging
    public void takeScreenshot(String filePath) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver();
            File screenshot = ts.getScreenshotAs(OutputType.FILE);
            File destination = new File(filePath);
            FileUtils.copyFile(screenshot, destination);
            LOGGER.info("Screenshot taken and saved to: {}", filePath);
        } catch (IOException e) {
            LOGGER.error("Error taking screenshot: {}", e.getMessage());
            throw new RuntimeException("Failed to take screenshot", e);
        }
    }

    // Switch Window by title with logging
    public void switchToWindowByTitle(String windowTitle) {
        try {
            String originalWindow = driver().getWindowHandle();
            for (String windowHandle : driver().getWindowHandles()) {
                driver().switchTo().window(windowHandle);
                if (driver().getTitle().equals(windowTitle)) {
                    LOGGER.info("Switched to window with title: {}", windowTitle);
                    return;
                }
            }
            driver().switchTo().window(originalWindow);
            LOGGER.warn("No window found with title: {}", windowTitle);
        } catch (Exception e) {
            LOGGER.error("Error switching to window with title {}: {}", windowTitle, e.getMessage());
            throw new RuntimeException("Failed to switch to window with title: " + windowTitle, e);
        }
    }

    // Switch to frame by locator with logging
    public void switchToFrame(By locator) {
        try {
            WebElement frameElement = waitForElementVisible(locator, DEFAULT_WAIT_SECONDS);
            if (frameElement != null) {
                driver().switchTo().frame(frameElement);
                LOGGER.info("Switched to frame: {}", locator.toString());
            } else {
                LOGGER.warn("Frame element not found or not visible: {}", locator.toString());
            }
        } catch (Exception e) {
            LOGGER.error("Error switching to frame {}: {}", locator.toString(), e.getMessage());
            throw new RuntimeException("Failed to switch to frame: " + locator, e);
        }
    }

    // Switch back to default content with logging
    public void switchToDefaultContent() {
        try {
            driver().switchTo().defaultContent();
            LOGGER.info("Switched back to default content");
        } catch (Exception e) {
            LOGGER.error("Error switching back to default content: {}", e.getMessage());
            throw new RuntimeException("Failed to switch back to default content", e);
        }
    }

    // Mouse hover over an element with logging
    public void mouseHover(By locator) {
        try {
            WebElement element = waitForElementVisible(locator, DEFAULT_WAIT_SECONDS);
            if (element != null) {
                Actions actions = new Actions(driver());
                actions.moveToElement(element).perform();
                LOGGER.info("Mouse hovered over element: {}", locator.toString());
            } else {
                LOGGER.warn("Element not found or not visible for mouse hover: {}", locator.toString());
            }
        } catch (Exception e) {
            LOGGER.error("Error performing mouse hover on element {}: {}", locator.toString(), e.getMessage());
            throw new RuntimeException("Failed to perform mouse hover on element: " + locator, e);
        }
    }

    // Alert handling with logging
    public void acceptAlert() {
        try {
            WebDriverWait wait = new WebDriverWait(driver(), Duration.ofSeconds(DEFAULT_WAIT_SECONDS));
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver().switchTo().alert();
            alert.accept();
            LOGGER.info("Alert accepted");
        } catch (NoAlertPresentException e) {
            LOGGER.warn("No alert present to accept");
        } catch (Exception e) {
            LOGGER.error("Error accepting alert: {}", e.getMessage());
            throw new RuntimeException("Failed to accept alert", e);
        }
    }

    // Dismiss alert with logging
    public void dismissAlert() {
        try {
            WebDriverWait wait = new WebDriverWait(driver(), Duration.ofSeconds(DEFAULT_WAIT_SECONDS));
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver().switchTo().alert();
            alert.dismiss();
            LOGGER.info("Alert dismissed");
        } catch (NoAlertPresentException e) {
            LOGGER.warn("No alert present to dismiss");
        } catch (Exception e) {
            LOGGER.error("Error dismissing alert: {}", e.getMessage());
            throw new RuntimeException("Failed to dismiss alert", e);
        }
    }

    // Get alert text with logging
    public String getAlertText() {
        try {
            WebDriverWait wait = new WebDriverWait(driver(), Duration.ofSeconds(DEFAULT_WAIT_SECONDS));
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver().switchTo().alert();
            String alertText = alert.getText();
            LOGGER.info("Retrieved alert text: {}", alertText);
            return alertText;
        } catch (NoAlertPresentException e) {
            LOGGER.warn("No alert present to get text from");
            return null;
        } catch (Exception e) {
            LOGGER.error("Error getting alert text: {}", e.getMessage());
            throw new RuntimeException("Failed to get alert text", e);
        }
    }

    //scroll to element with logging
    public void scrollToElement(By locator) {
        try {
            WebElement element = waitForElementVisible(locator, DEFAULT_WAIT_SECONDS);
            if (element != null) {
                ((JavascriptExecutor) driver()).executeScript("arguments[0].scrollIntoView(true);", element);
                LOGGER.info("Scrolled to element: {}", locator.toString());
            } else {
                LOGGER.warn("Element not found or not visible for scrolling: {}", locator.toString());
            }
        } catch (Exception e) {
            LOGGER.error("Error scrolling to element {}: {}", locator.toString(), e.getMessage());
            throw new RuntimeException("Failed to scroll to element: " + locator, e);
        }
    }

    //scroll bottom of the page with logging
    public void scrollToBottom() {
        try {
            ((JavascriptExecutor) driver()).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            LOGGER.info("Scrolled to bottom of the page");
        } catch (Exception e) {
            LOGGER.error("Error scrolling to bottom of the page: {}", e.getMessage());
            throw new RuntimeException("Failed to scroll to bottom of the page", e);
        }
    }

    //Web table handling with logging
    public String getCellValueFromTable(By tableLocator, String searchText, int columnIndex) {
        try {
            WebElement table = waitForElementVisible(tableLocator, DEFAULT_WAIT_SECONDS);
            if (table != null) {
                List<WebElement> rows = table.findElements(By.tagName("tr"));
                for (WebElement row : rows) {
                    List<WebElement> cells = row.findElements(By.tagName("td"));
                    if (!cells.isEmpty() && cells.get(0).getText().equals(searchText)) {
                        String cellValue = cells.get(columnIndex).getText();
                        LOGGER.info("Retrieved cell value '{}' from table at locator {} for search text '{}'", cellValue, tableLocator.toString(), searchText);
                        return cellValue;
                    }
                }
                LOGGER.warn("Search text '{}' not found in any row of the table at locator {}", searchText, tableLocator.toString());
                return null;
            } else {
                LOGGER.warn("Table element not found or not visible at locator: {}", tableLocator.toString());
                return null;
            }
        } catch (Exception e) {
            LOGGER.error("Error retrieving cell value from table {}: {}", tableLocator.toString(), e.getMessage());
            throw new RuntimeException("Failed to get cell value from table: " + tableLocator, e);
        }

    }

    // Web table row and column number with logging
    public int getRowCountFromTable(By tableLocator) {
        try {
            WebElement table = waitForElementVisible(tableLocator, DEFAULT_WAIT_SECONDS);
            if (table != null) {
                List<WebElement> rows = table.findElements(By.tagName("tr"));
                int rowCount = rows.size();
                LOGGER.info("Retrieved row count from table at locator {}: {}", tableLocator.toString(), rowCount);
                return rowCount;
            } else {
                LOGGER.warn("Table element not found or not visible at locator: {}", tableLocator.toString());
                return 0;
            }
        } catch (Exception e) {
            LOGGER.error("Error retrieving row count from table {}: {}", tableLocator.toString(), e.getMessage());
            throw new RuntimeException("Failed to get row count from table: " + tableLocator, e);
        }
    }

    // Get column count from table with logging
    public int getColumnCountFromTable(By tableLocator) {
        try {
            WebElement table = waitForElementVisible(tableLocator, DEFAULT_WAIT_SECONDS);
            if (table != null) {
                List<WebElement> rows = table.findElements(By.tagName("tr"));
                if (!rows.isEmpty()) {
                    List<WebElement> columns = rows.get(0).findElements(By.tagName("td"));
                    int columnCount = columns.size();
                    LOGGER.info("Retrieved column count from table at locator {}: {}", tableLocator.toString(), columnCount);
                    return columnCount;
                } else {
                    LOGGER.warn("No rows found in the table at locator: {}", tableLocator.toString());
                    return 0;
                }
            } else {
                LOGGER.warn("Table element not found or not visible at locator: {}", tableLocator.toString());
                return 0;
            }
        } catch (Exception e) {
            LOGGER.error("Error retrieving column count from table {}: {}", tableLocator.toString(), e.getMessage());
            throw new RuntimeException("Failed to get column count from table: " + tableLocator, e);
        }
    }

    // Get Current TimeStamp with perticular format
    public String getCurrentTimeStamp(String format) {
        try {
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern(format);
            String timestamp = java.time.LocalDateTime.now().format(formatter);
            LOGGER.info("Retrieved current timestamp with format '{}': {}", format, timestamp);
            return timestamp;
        } catch (Exception e) {
            LOGGER.error("Error retrieving current timestamp with format {}: {}", format, e.getMessage());
            throw new RuntimeException("Failed to get current timestamp with format: " + format, e);
        }
    }

}
