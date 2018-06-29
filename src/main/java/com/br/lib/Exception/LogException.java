package com.br.lib.Exception;

public class LogException extends Exception {
    public  LogException(String message){
        super(message);
    }
    public  LogException(String message,Exception e){
        super(message,e);
    }
}
