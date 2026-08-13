package com.utils;


import org.apache.logging.log4j.Logger;

public class DataUtils {

    private static final Logger LOGGER = LoggerUtils.getLogger(DataUtils.class);

    public static String currentTimeStamp(String pattern) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(pattern);
        LOGGER.info("Current timestamp: " + sdf.format(new java.util.Date()));
        return sdf.format(new java.util.Date());
    }
    public static String currentTimeStamp() {
        LOGGER.info("Current timestamp: " + currentTimeStamp("yyyy-MM-dd HH:mm:ss"));
        return currentTimeStamp("yyyy-MM-dd HH:mm:ss");
    }
}
