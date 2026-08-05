package com.exceptions;

import java.awt.*;

public class FrameworkException extends RuntimeException{
    public FrameworkException(String message){
        super(message);
    }
    public FrameworkException(String message, Throwable cause){
        super(message, cause);
    }
}
