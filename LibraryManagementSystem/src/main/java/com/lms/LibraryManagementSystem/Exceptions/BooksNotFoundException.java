package com.lms.LibraryManagementSystem.Exceptions;

public class BooksNotFoundException extends Exception{
    public BooksNotFoundException(String error){
        super(error);
    }
}
