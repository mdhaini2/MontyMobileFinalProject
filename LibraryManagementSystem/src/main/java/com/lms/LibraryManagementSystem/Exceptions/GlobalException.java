package com.lms.LibraryManagementSystem.Exceptions;


import com.google.i18n.phonenumbers.NumberParseException;
import com.lms.LibraryManagementSystem.Utils.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler
    public ResponseEntity<ResponseError> handlePhoneNumberInvalidException(PhoneNumberInvalidException exception) {
        ResponseError errorObject = new ResponseError();
        errorObject.setStatus(HttpStatus.BAD_REQUEST.value());
        errorObject.setMessage(exception.getMessage());
        errorObject.setTime(System.currentTimeMillis());
        return new ResponseEntity<ResponseError>(errorObject, HttpStatus.BAD_REQUEST);

    }   @ExceptionHandler
    public ResponseEntity<ResponseError> handleNumberParseException(NumberParseException exception) {
        ResponseError errorObject = new ResponseError();
        errorObject.setStatus(HttpStatus.BAD_REQUEST.value());
        errorObject.setMessage(exception.getMessage());
        errorObject.setStackTrace(exception.getStackTrace());
        errorObject.setTime(System.currentTimeMillis());
        return new ResponseEntity<ResponseError>(errorObject, HttpStatus.BAD_REQUEST);

    }  @ExceptionHandler
    public ResponseEntity<ResponseError> handleSQLException(SQLException exception) {
        ResponseError errorObject = new ResponseError();
        errorObject.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorObject.setMessage(exception.getMessage());
        errorObject.setStackTrace(exception.getStackTrace());
        errorObject.setTime(System.currentTimeMillis());
        return new ResponseEntity<ResponseError>(errorObject, HttpStatus.BAD_REQUEST);

    }


}
