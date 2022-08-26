package com.lms.User.Controllers;

import com.lms.User.Exceptions.IncorrectOTPException;
import com.lms.User.Exceptions.InvalidUserException;
import com.lms.User.Exceptions.OTPNotReceivedException;
import com.lms.User.Services.OTPConfirmationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    OTPConfirmationService otpConfirmationService;
    @PostMapping(value = "/confirmOTP")
    public Object confirmOTP(@RequestParam int userId, @RequestParam int otp) throws InvalidUserException, OTPNotReceivedException, IncorrectOTPException {
        return  otpConfirmationService.confirmOTP(userId,otp);
    }
}
