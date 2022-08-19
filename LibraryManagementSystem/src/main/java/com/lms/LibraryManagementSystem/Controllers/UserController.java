package com.lms.LibraryManagementSystem.Controllers;

import com.google.i18n.phonenumbers.NumberParseException;
import com.lms.LibraryManagementSystem.Entities.Users;
import com.lms.LibraryManagementSystem.Exceptions.PhoneNumberInvalidException;
import com.lms.LibraryManagementSystem.Services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServices userServices;

    @PostMapping(value = "/register")
    public Object registerUser(@RequestBody Users user) throws NumberParseException, PhoneNumberInvalidException {
        return userServices.registerUser(user);
    }
}
