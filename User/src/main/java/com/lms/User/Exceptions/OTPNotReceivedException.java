package com.lms.User.Exceptions;

public class OTPNotReceivedException extends Exception {
    public OTPNotReceivedException(String error){
        super(error);
    }
}
