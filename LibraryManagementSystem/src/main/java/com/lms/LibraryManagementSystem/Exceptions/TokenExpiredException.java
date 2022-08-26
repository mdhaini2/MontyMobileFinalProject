package com.lms.LibraryManagementSystem.Exceptions;

public class TokenExpiredException extends Exception{
    public TokenExpiredException(String error){
        super(error);
    }
}
