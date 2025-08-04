package Listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import Base.TestBase;
import Utilities.ConfigReader;

public class ReportListeners extends TestBase implements ITestListener{
	private static Logger log = LogManager.getLogger(ReportListeners.class);
	public static ExtentReports report;
	public static ExtentTest test;
	
	public void onTestStart(ITestResult result) {
		log.info("Test authored by: '"+ConfigReader.getValue("QA", "author")+"'"
				+ ".");
		log.info("Test case '"+ result.getMethod().getMethodName() +"' execution started.");
		log.info("Test description - '"+ result.getMethod().getDescription() +"'.");
		
		test = report.createTest(
				result.getTestClass().getRealClass().getName()+" :: "+result.getMethod().getMethodName());
		
		test.log(Status.INFO, "Navigate to url: '"+ConfigReader.getValue("QA", "url") +"' .");
		test.log(Status.INFO, "'"+ConfigReader.getValue("QA","browser")+"' Browser Launched.");
		test.log(Status.INFO, "Test case '"+result.getMethod().getMethodName()+"' execution started.");
		test.log(Status.INFO, "Test description '"+result.getMethod().getDescription()+"'.");
	}
	
	public void onTestSuccess(ITestResult result) {
		log.info("Test case '"+result.getMethod().getMethodName()+"' execution started.");
		
		Markup markup = MarkupHelper.createLabel("Test case '"+ result.getMethod().getMethodName() +
				"', execution passed.", ExtentColor.GREEN);
		
		test.log(Status.PASS, markup);
		test.log(Status.INFO, "'"+ConfigReader.getValue("QA", "browser")+"' browser quit successfully.");
	}
	
	public void onTestfailure(ITestResult result) {
		log.info("Test case '"+result.getMethod().getMethodName()+"' execution failed.");
		log.info("Exception occured: "+result.getThrowable());	
	}
}














