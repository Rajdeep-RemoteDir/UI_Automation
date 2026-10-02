package com.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.constants.FrameworkConstants;

public class ExtentReportManager {
    private static volatile ExtentReports extent;
    private static  final Object LOCK = new Object();

    private ExtentReportManager() {
        // private constructor to prevent instantiation
    }
    public static ExtentReports getInstance() {
        if (extent == null) {
            synchronized (LOCK) {
                if (extent == null) {
                    ExtentSparkReporter spark = new ExtentSparkReporter(FrameworkConstants.EXTENT_REPORT_DIR+"TestReport.html");
                    spark.config().setReportName("Automation Test Results");
                    spark.config().setDocumentTitle("Automation Test Report");
                    extent = new ExtentReports();
                    extent.attachReporter(spark);
                }
            }
        }
        return extent;
    }

    public static void flush() {
        if (extent != null) {
            extent.flush();
        }
    }

}
