package com.lms.User.Exceptions;

public class InvalidUserException extends Exception{
    public InvalidUserException(String error){
        super(error);
    }
}
