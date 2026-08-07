package com.config;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties prperties = new Properties();

    static{
        //getproperty checks if the property is set in the system properties, if not it will return the default value "qa"
        String env = System.getProperty("env", "qa");
        String configFilePath = "src/test/resources/config_/" + env + ".properties";

        try(FileInputStream fis = new FileInputStream(configFilePath)) {
            prperties.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String get(String key){
        String value = prperties.getProperty(key);
        if(value==null){
            throw new RuntimeException("Property "+key+" is not specified in the config file");
        }
        return value;
    }


}
