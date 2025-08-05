package Base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aventstack.extentreports.Status;

import Listeners.ReportListeners;
import io.github.bonigarcia.wdm.WebDriverManager;

public class TestBase {
	
	
	public static WebDriver driver;
	public static Properties prop;
	public static Map<String,String> globalStorage = new HashMap<>();
	private static final Logger log = LogManager.getLogger(TestBase.class);
	
	
	public TestBase() {
		try {
			prop = new Properties();
			FileInputStream file = new FileInputStream("C:\\Users\\Acer\\Desktop\\UIAutomation_Project\\"
					+ "UI_Automation_Test\\src\\main\\java\\configuration\\QA-config.properties");
			prop.load(file);
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void initializeDriver() {
		
		String browserName = prop.getProperty("browser");
		
		if(browserName.equalsIgnoreCase("Chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		}
		else if(browserName.equalsIgnoreCase("Firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		}
		else if(browserName.equalsIgnoreCase("Edge")) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
		}
		else {
			driver = null;
		}
		
		if(!driver.equals(null)) {
			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Utilities.TestUtils.IMPLICITLY_WAIT));
			String url = prop.getProperty("url");
			driver.get(url);
		}
		else {
			log.error("Browser doesn't initializad.");
			ReportListeners.test.log(Status.FAIL, "Browser doesn't initialized.");
		}
	}

	public static String captureScreenshot(String name) {
		File source = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		String fileName = "Screenshot_"+name+"_"+new SimpleDateFormat("ddMMyy_HHmmss").format(new Date())+".png";
		String destinationPath = System.getProperty("user.dir")+"screenshots\\"+fileName;
		try {
			FileUtils.copyFile(source, new File(destinationPath));
		}
		catch(IOException e) {
			throw new RuntimeException("Error occured while copying screenshot to "+destinationPath);
		}
		return destinationPath;
	}
	
	
	public static void logMessage(String type,String message) {
		try {
			if(type.toUpperCase().equals("INFO")){
				log.info(message);
				ReportListeners.test.log(Status.INFO, message);
			}
			else if(type.toUpperCase().equals("FAIL")){
				log.error(message);
				ReportListeners.test.log(Status.FAIL, message);
			}
			else if(type.toUpperCase().equals("WARNING")){
				log.warn(message);
				ReportListeners.test.log(Status.WARNING, message);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void ScrollTpElement(WebElement ele1) {
		try {
			Thread.sleep(1000);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView({ behavior: 'smooth', block: 'center'})", ele1);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean findElement(WebElement element,int waitTime_sec) {
		boolean output=false;
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitTime_sec));
			wait.until(ExpectedConditions.visibilityOf(element));
			output = element.isDisplayed();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return output;
	}
	
	public void enterTxt(WebElement element,String value) {
		try {
			ScrollTpElement(element);
			if(element.isDisplayed()) {
				element.clear();
				element.sendKeys(value);
			}
			else {
				logMessage("FAIL", element.getText()+" is not visible.");
			}
		}
		catch(Exception e) {
			logMessage("FAIL", "Unable to enter text due to : "+e.getLocalizedMessage());
		}
	}
	
	public static void putValue(String veriableName,String veriableValue) {
		try {
			if(!globalStorage.containsKey(veriableName)) {
				globalStorage.put(veriableName, veriableValue);
				logMessage("INFO", "VALUE STORED   "+veriableName+" :: "+veriableValue);
			}
		}
		catch(Exception e) {
			logMessage("FAIL", "Unable to store the value as "+veriableName+" :: "+veriableValue+" due to : "+e.getLocalizedMessage());
		}
	}
	
	public static String getValue(String veriableName) {
		String result="";
		try {
			if(globalStorage.containsKey(veriableName)) {
				result = globalStorage.get(veriableName);
			}
		}
		catch(Exception e) {
			logMessage("FAIL", "Unable to get the value due to "+e.getLocalizedMessage());
		}
		return result;
	}
	
}
