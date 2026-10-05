package com.utils;

import org.apache.logging.log4j.Logger;

public class Encription extends TestBase{

    private static final Logger LOGGER = LoggerUtils.getLogger(Encription.class);

    public static String encrypt(String input) {
        StringBuilder encrypted = new StringBuilder();
        for (char c : input.toCharArray()) {
            encrypted.append((char) (c + 3));

        }
        LOGGER.info("Text Encrypted Successfully, Thank you");// Simple encryption by shifting characters
        return encrypted.toString();
    }

    public static void main(String[] args) {
        String password = "rajdeep.gupta1001@gmail.com";
        String encryptedPassword = encrypt(password);
        System.out.println("Original Password: " + password);
        System.out.println("Encrypted Password: " + encryptedPassword);
    }

}
