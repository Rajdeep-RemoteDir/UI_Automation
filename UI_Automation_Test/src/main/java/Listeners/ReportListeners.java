package Listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
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
import Utilities.ExtentReportManager;

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
		
		String screenshotPath = TestBase.captureScreenshot(result.getMethod().getMethodName());
		log.info("Screenshot saved at: "+screenshotPath);
		Markup markup = MarkupHelper.createLabel("Test case '"+result.getMethod().getMethodName()
				+"' execution failed.", ExtentColor.RED);
		test.log(Status.FAIL, markup);
		test.log(Status.INFO, result.getThrowable());
		test.addScreenCaptureFromPath(screenshotPath);
		test.log(Status.INFO, "'"+ConfigReader.getValue("QA", "browser")+"' browser quit successfully.");
		
	}
	
	public void onTestSkipped(ITestResult result) {
		log.info("Test case '"+result.getMethod().getMethodName()+"' execution skipped.");
		log.info("Exception occured: "+result.getThrowable());
		Markup markup = MarkupHelper.createLabel("Test case '"+result.getMethod().getMethodName()+"' execution skipped.", ExtentColor.ORANGE);
		
		test.log(Status.SKIP, markup);
		test.log(Status.INFO, result.getThrowable());
		test.log(Status.INFO, "'"+ConfigReader.getValue("QA", "browser")+"' browser quit successfully.");
		
	}
	
	public void onStart(ITestContext context) {
		log.info("Test Suite execution started.");
		report = ExtentReportManager.setupExtentReport();
	}
	
	public void onFinish(ITestContext context) {
		log.info("Test suite execution ended.");
		report.flush();
		log.info("ExtentReport generated.");
	}
}














