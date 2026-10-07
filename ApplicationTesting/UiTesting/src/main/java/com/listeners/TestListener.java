package com.listeners;

import com.reports.ExtentReportManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("STARTED: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("PASSED: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("FAILED: " + result.getName());
        // Screenshot capture on failure is also handled in Hooks.java for Cucumber scenarios.
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("SKIPPED: " + result.getName());
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("Suite started: " + context.getName());
        try {
            // Ensure ExtentReports is initialized even if Cucumber listener isn't registered
            ExtentReportManager.getInstance();
            System.out.println("ExtentReports initialized by TestListener.onStart");
        } catch (Throwable t) {
            System.out.println("Failed to initialize ExtentReports in TestListener: " + t.getMessage());
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Suite finished: " + context.getName());
        try {
            ExtentReportManager.flush();
            System.out.println("ExtentReports flushed by TestListener.onFinish");
        } catch (Throwable t) {
            System.out.println("Failed to flush ExtentReports in TestListener: " + t.getMessage());
        }
    }
}
