package com.lms.LibraryManagementSystem.Controllers;

import com.google.i18n.phonenumbers.NumberParseException;
import com.lms.LibraryManagementSystem.Entities.Users;
import com.lms.LibraryManagementSystem.Exceptions.CredentialsNotValidException;
import com.lms.LibraryManagementSystem.Exceptions.PhoneNumberInvalidException;
import com.lms.LibraryManagementSystem.Services.UserServices;
import com.lms.LibraryManagementSystem.Utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-server")
public class UserController {
    @Autowired
    private UserServices userServices;

    @PostMapping(value = "/register")
    public Response registerUser(@RequestBody Users user) throws NumberParseException, PhoneNumberInvalidException {
        return userServices.registerUser(user);
    }
    @GetMapping(value = "/login")
    public Response loginUser(@RequestParam String phoneNumber, @RequestParam String password) throws PhoneNumberInvalidException, NumberParseException, CredentialsNotValidException {
        return userServices.loginUser(phoneNumber,password);
    }

    @PutMapping(value = "/updateUserProfile")
    public Response updateUserProfile(@RequestBody Users user){
       return  userServices.updateUserProfile(user);
    }
    @PutMapping(value = "/changeUserPassword")
    public Response updateUserProfile(@RequestParam String oldPassword, @RequestParam String newPassword) throws CredentialsNotValidException {
       return  userServices.changePassword(oldPassword,newPassword);
    }
}
