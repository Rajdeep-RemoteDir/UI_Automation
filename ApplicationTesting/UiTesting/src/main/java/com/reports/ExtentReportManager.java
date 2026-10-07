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
                    try {
                        // Ensure directory exists
                        java.io.File dir = new java.io.File(FrameworkConstants.EXTENT_REPORT_DIR);
                        if (!dir.exists()) {
                            boolean created = dir.mkdirs();
                            System.out.println("ExtentReport directory created: " + dir.getAbsolutePath() + " (created=" + created + ")");
                        }

                        String reportPath = FrameworkConstants.EXTENT_REPORT_DIR + "TestReport.html";
                        System.out.println("Initializing Extent report at: " + reportPath);
                        com.aventstack.extentreports.reporter.ExtentSparkReporter spark = new com.aventstack.extentreports.reporter.ExtentSparkReporter(reportPath);
                        spark.config().setReportName("Automation Test Results");
                        spark.config().setDocumentTitle("Automation Test Report");
                        extent = new com.aventstack.extentreports.ExtentReports();
                        extent.attachReporter(spark);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to initialize ExtentReports", e);
                    }
                }
            }
        }
        return extent;
    }

    public static void flush() {
        if (extent != null) {
            try {
                System.out.println("Flushing Extent report to disk.");
                extent.flush();
                System.out.println("Extent report flushed.");
            } catch (Exception e) {
                System.out.println("Error while flushing Extent report: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("ExtentReports instance is null; nothing to flush.");
        }
    }

}
