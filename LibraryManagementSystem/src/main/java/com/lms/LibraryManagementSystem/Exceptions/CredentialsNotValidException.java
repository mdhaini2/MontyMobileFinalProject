package com.lms.LibraryManagementSystem.Exceptions;

public class CredentialsNotValidException extends Exception{
    public CredentialsNotValidException(String error){
        super(error);
    }
}
