package com.lms.LibraryManagementSystem.Exceptions;

public class UserAlreadyReservedBookException extends Exception{
    public UserAlreadyReservedBookException(String error){
        super(error);
    }
}
