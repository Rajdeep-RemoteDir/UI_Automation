package com.constants;

public class FrameworkConstants {
    public static final long EXPLICIT_WAIT_SECONDS = 10;
    public static final int PAGE_LOAD_TIMEOUT_SECONDS = 30;

    private static final String USER_DIR = System.getProperty("user.dir");
    public static final String SCREENSHOT_DIR = USER_DIR + System.getProperty("file.separator") + "screenshots" + System.getProperty("file.separator");
    public static final String EXTENT_REPORT_DIR = USER_DIR + System.getProperty("file.separator") + "test-output" + System.getProperty("file.separator") + "ExtentReport" + System.getProperty("file.separator");

}
