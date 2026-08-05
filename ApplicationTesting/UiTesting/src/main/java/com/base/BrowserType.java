package com.base;

import com.exceptions.FrameworkException;

public enum BrowserType {

    CHROME,
    CHROME_HEADLESS,
    FIREFOX,
    FIREFOX_HEADLESS,
    EDGE,
    EDGE_HEADLESS,
    SAFARI,
    SAFARI_HEADLESS;

    public static BrowserType fromString(String value){
        if(value == null || value.trim().isEmpty()){
            throw new FrameworkException("Browser type value is null or empty");
        }
        String normalized = value.trim().toUpperCase().replace("-", "_");
        for(BrowserType browserType : BrowserType.values()){
            if(browserType.name().equals(normalized)){
                return browserType;
            }
        }
        throw new FrameworkException("Invalid browser type value: " + value);

    }

    public boolean isHeadless(){
        return this == CHROME_HEADLESS || this == FIREFOX_HEADLESS || this == EDGE_HEADLESS || this == SAFARI_HEADLESS;
    }
}
