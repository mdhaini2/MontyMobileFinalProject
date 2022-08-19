package com.lms.LibraryManagementSystem.Services;



import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.lms.LibraryManagementSystem.Entities.OTP;
import com.lms.LibraryManagementSystem.Entities.Users;
import com.lms.LibraryManagementSystem.Exceptions.PhoneNumberInvalidException;
import com.lms.LibraryManagementSystem.Producers.ProducerOTP;
import com.lms.LibraryManagementSystem.Repositories.UserRepository;
import com.lms.LibraryManagementSystem.Utils.Response;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Log4j2
public class UserServices {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProducerOTP producerOTP;
    PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();  ;

    private int okStatus = HttpStatus.OK.value();
    public Object registerUser(Users user) throws NumberParseException, PhoneNumberInvalidException {
        log.info(user.getPhoneNumber());
        String userPhoneNumber = String.valueOf(user.getPhoneNumber());
        Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(userPhoneNumber,
                Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name());
        if(!phoneNumberUtil.isValidNumber(phoneNumber)){
            throw new PhoneNumberInvalidException(user.getPhoneNumber()+ " is not a valid phone number");
        }

        user.setIsRegistered(0);
        userRepository.save(user);

        OTP otp = new OTP(user.getUserID(),generateOTP());
        producerOTP.sendJsonMessage(otp);

        String responseMessage = "Sent OTP to user";
        long timeStamp = System.currentTimeMillis();
        Response response = new Response(okStatus,responseMessage,timeStamp,user);

        return response;
    }
    public int generateOTP(){
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return number;
    }
}
