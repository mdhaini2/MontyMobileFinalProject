package com.lms.User.Exceptions;

public class IncorrectOTPException extends Exception{
    public IncorrectOTPException(String error){
        super(error);
    }
}
