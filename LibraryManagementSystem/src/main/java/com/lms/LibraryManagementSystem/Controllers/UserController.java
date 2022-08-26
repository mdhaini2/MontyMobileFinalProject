package com.lms.LibraryManagementSystem.Controllers;

import com.google.i18n.phonenumbers.NumberParseException;
import com.lms.LibraryManagementSystem.Entities.Users;
import com.lms.LibraryManagementSystem.Exceptions.CredentialsNotValidException;
import com.lms.LibraryManagementSystem.Exceptions.PhoneNumberInvalidException;
import com.lms.LibraryManagementSystem.Services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-server")
public class UserController {
    @Autowired
    private UserServices userServices;

    @PostMapping(value = "/register")
    public Object registerUser(@RequestBody Users user) throws NumberParseException, PhoneNumberInvalidException {
        return userServices.registerUser(user);
    }
    @GetMapping(value = "/login")
    public Object loginUser(@RequestParam String phoneNumber, @RequestParam String password) throws PhoneNumberInvalidException, NumberParseException, CredentialsNotValidException {
        return userServices.loginUser(phoneNumber,password);
    }

    @PutMapping(value = "/updateUserProfile")
    public Object updateUserProfile(@RequestBody Users user){
       return  userServices.updateUserProfile(user);
    }
    @PutMapping(value = "/changeUserPassword")
    public Object updateUserProfile(@RequestParam String oldPassword, @RequestParam String newPassword) throws CredentialsNotValidException {
       return  userServices.changePassword(oldPassword,newPassword);
    }
}
