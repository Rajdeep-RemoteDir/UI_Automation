package com.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.base.DriverManager;
import com.reports.ExtentReportManager;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventHandler;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.TestCase;
import io.cucumber.plugin.event.TestCaseFinished;
import io.cucumber.plugin.event.TestCaseStarted;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.TestStepFinished;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ExtentReportListener implements ConcurrentEventListener {

    private static final Map<String, ExtentTest> SCENARIO_TEST_MAP = new ConcurrentHashMap<>();
    private static final ThreadLocal<ExtentTest> CURRENT_TEST = new ThreadLocal<>();

    public static void logInfo(String message) {
        log(message, Status.INFO, true);
    }

    public static void logError(String message) {
        log(message, Status.FAIL, true);
    }

    private static void log(String message, Status status, boolean attachScreenshot) {
        ExtentTest test = CURRENT_TEST.get();
        if (test == null) {
            return;
        }

        if (attachScreenshot) {
            String screenshotBase64 = captureScreenshotBase64();
            if (screenshotBase64 != null) {
                test.log(status, message, MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64).build());
                return;
            }
        }

        test.log(status, message);
    }

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseStarted.class, new TestCaseStartedHandler());
        publisher.registerHandlerFor(TestStepFinished.class, new TestStepFinishedHandler());
        publisher.registerHandlerFor(TestCaseFinished.class, new TestCaseFinishedHandler());
        publisher.registerHandlerFor(TestRunFinished.class, new TestRunFinishedHandler());
    }

    private static class TestCaseStartedHandler implements EventHandler<TestCaseStarted> {
        @Override
        public void receive(TestCaseStarted event) {
            TestCase testCase = event.getTestCase();
            String scenarioName = testCase.getName();
            ExtentTest test = ExtentReportManager.getInstance().createTest(scenarioName);
            SCENARIO_TEST_MAP.put(scenarioKey(testCase), test);
            CURRENT_TEST.set(test);
        }
    }

    private static class TestStepFinishedHandler implements EventHandler<TestStepFinished> {
        @Override
        public void receive(TestStepFinished event) {
            // Keep the report focused on explicit custom log entries only.
            // Do not log the generic Cucumber step text from the feature file.
            if (!(event.getTestStep() instanceof PickleStepTestStep)) {
                return;
            }

            PickleStepTestStep step = (PickleStepTestStep) event.getTestStep();
            ExtentTest test = SCENARIO_TEST_MAP.get(scenarioKey(event.getTestCase()));
            if (test == null) {
                return;
            }

            if (event.getResult() != null && event.getResult().getError() != null) {
                String screenshotBase64 = captureScreenshotBase64();
                if (screenshotBase64 != null) {
                    test.log(Status.FAIL, "Error: " + event.getResult().getError().toString(),
                            MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64).build());
                } else {
                    test.log(Status.FAIL, "Error: " + event.getResult().getError().toString());
                }
            }
        }
    }

    private static class TestCaseFinishedHandler implements EventHandler<TestCaseFinished> {
        @Override
        public void receive(TestCaseFinished event) {
            ExtentTest test = SCENARIO_TEST_MAP.get(scenarioKey(event.getTestCase()));
            if (test != null && event.getResult() != null && event.getResult().getStatus() == io.cucumber.plugin.event.Status.FAILED) {
                String screenshotBase64 = captureScreenshotBase64();
                if (screenshotBase64 != null) {
                    test.log(Status.FAIL, "Scenario failed", MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64).build());
                } else {
                    test.log(Status.FAIL, "Scenario failed");
                }
            }
            SCENARIO_TEST_MAP.remove(scenarioKey(event.getTestCase()));
            CURRENT_TEST.remove();
        }
    }

    private static class TestRunFinishedHandler implements EventHandler<TestRunFinished> {
        @Override
        public void receive(TestRunFinished event) {
            ExtentReportManager.flush();
        }
    }

    private static String scenarioKey(TestCase testCase) {
        return testCase.getUri().toString() + ":" + testCase.getLine() + ":" + Thread.currentThread().getId();
    }

    private static Status mapStatus(io.cucumber.plugin.event.Status cucumberStatus) {
        switch (cucumberStatus) {
            case PASSED:
                return Status.PASS;
            case FAILED:
                return Status.FAIL;
            case SKIPPED:
                return Status.SKIP;
            case PENDING:
            case UNDEFINED:
                return Status.WARNING;
            case AMBIGUOUS:
            default:
                return Status.INFO;
        }
    }

    private static String captureScreenshotBase64() {
        if (!DriverManager.isDriverintialized()) {
            return null;
        }
        try {
            return ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            return null;
        }
    }
}
