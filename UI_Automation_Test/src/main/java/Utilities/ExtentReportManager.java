package Utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import Base.TestBase;

public class ExtentReportManager extends TestBase{
	
	public static ExtentReports extentReport;
	public static String extentReportFile;
	
	public static ExtentReports setupExtentReport() {
		try {
			extentReportFile = System.getProperty("user.dir")+"\\TestReports\\ExecutionReport.html";
			ExtentSparkReporter sparkReporter = new ExtentSparkReporter(extentReportFile);
			sparkReporter.config().setReportName("Test Automation Report");
			sparkReporter.config().setDocumentTitle("Execution Report");
			
			extentReport = new ExtentReports();
			extentReport.attachReporter(sparkReporter);
			extentReport.setSystemInfo("Test URL", ConfigReader.getValue("QA", "url"));
			extentReport.setSystemInfo("Operating System", ConfigReader.getValue("QA", "browser")+ " Browser");
			extentReport.setSystemInfo("Tester", System.getProperty("user.name"));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return extentReport;
	}
}
