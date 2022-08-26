package com.lms.LibraryManagementSystem.Services;


import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.lms.LibraryManagementSystem.Entities.OTP;
import com.lms.LibraryManagementSystem.Entities.Users;
import com.lms.LibraryManagementSystem.Exceptions.CredentialsNotValidException;
import com.lms.LibraryManagementSystem.Exceptions.PhoneNumberInvalidException;
import com.lms.LibraryManagementSystem.Producers.ProducerOTP;
import com.lms.LibraryManagementSystem.Repositories.UserRepository;
import com.lms.LibraryManagementSystem.Utils.JwtUtil;
import com.lms.LibraryManagementSystem.Utils.Response;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

@Service
@Log4j2
public class UserServices {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProducerOTP producerOTP;

    @Autowired
    private MyUserDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtTokenUtil;
    @Autowired
    private HttpServletRequest request;

    @Value("${phone.country.code}")
    private int countryCode;


    public Object registerUser(Users user) throws NumberParseException, PhoneNumberInvalidException {
        // Phone number validator
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        String userPhoneNumber = String.valueOf(user.getPhoneNumber());
        Phonenumber.PhoneNumber number = new Phonenumber.PhoneNumber();

        number.setCountryCode(countryCode);
        char firstChar = userPhoneNumber.charAt(0);
        if(firstChar == '0'){
            number.setItalianLeadingZero(true);
            number.clearNumberOfLeadingZeros();
        }

        number.setNationalNumber(Long.valueOf(userPhoneNumber));
        log.info("Phone Number:" + String.valueOf(Long.valueOf(userPhoneNumber)));
        userPhoneNumber = String.valueOf(Long.valueOf(userPhoneNumber));
        user.setPhoneNumber(userPhoneNumber);
        log.info(phoneNumberUtil.isPossibleNumber(number));

        if (!phoneNumberUtil.isPossibleNumber(number)) {
            throw new PhoneNumberInvalidException(user.getPhoneNumber() + " is not a valid phone number");
        }

        Users existingUser = userRepository.findByPhoneNumber(user.getPhoneNumber());

        if (existingUser != null) {
            if (existingUser.getIsRegistered() == 1) {
                throw new PhoneNumberInvalidException("User with phone number: " + user.getPhoneNumber() + " already exists!");
            }
            userRepository.deleteById(existingUser.getUserID());
        }

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setIsRegistered(0);
        userRepository.save(user);

        OTP otp = new OTP(user.getUserID(), generateOTP());
        producerOTP.sendJsonMessage(otp);

        String responseMessage = "OTP sent to user";
        Response response = new Response(responseMessage, user);
        return response;

    }

    public int generateOTP() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return number;
    }

    public Object loginUser(String inputPhoneNumber, String password) throws PhoneNumberInvalidException, NumberParseException, CredentialsNotValidException {
        // Phone number validator
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        String userPhoneNumber = String.valueOf(inputPhoneNumber);
        Phonenumber.PhoneNumber number = new Phonenumber.PhoneNumber();
        number.setCountryCode(countryCode);
        number.setNationalNumber(Long.valueOf(userPhoneNumber));

        log.info(phoneNumberUtil.isPossibleNumber(number));
        if (!phoneNumberUtil.isPossibleNumber(number)) {
            throw new PhoneNumberInvalidException(inputPhoneNumber + " is not a valid phone number");
        }

        userPhoneNumber = String.valueOf(Long.valueOf(userPhoneNumber));
        Users user = userRepository.findByPhoneNumber(userPhoneNumber);
        log.info(user);

        // Password validator
        if (user == null) {
            throw new PhoneNumberInvalidException("User with phone number: " + userPhoneNumber + " does not exists!");
        }

        if (!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            throw new CredentialsNotValidException("Incorrect Password!");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getPhoneNumber());
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        Response response = new Response("User " + user.getFullName() + " logged in successfully", jwt);
        return response;
    }

    public Response updateUserProfile(Users updateUser) {

        Users user = getUserFromToken();

        user.setAddress(updateUser.getAddress());
        user.setEmail(updateUser.getEmail());
        user.setFullName(updateUser.getFullName());
        userRepository.save(user);

        Response response = new Response("User profile updated Successfully", user);
        return response;
    }


    public Response changePassword(String oldPassword, String newPassword) throws CredentialsNotValidException {

        Users user = getUserFromToken();

        if (!new BCryptPasswordEncoder().matches(oldPassword, user.getPassword())) {
            throw new CredentialsNotValidException("Incorrect Password");
        }

        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(user);

        Response response = new Response("Password changed successfully", user);
        return response;
    }

    public Users getUserFromToken() {

        String token = jwtTokenUtil.getToken(request);
        String phoneNumber = jwtTokenUtil.extractUsername(token);
        Users user = userRepository.findByPhoneNumber(phoneNumber);
        return user;
    }
}
