package com.lms.LibraryManagementSystem.Listeners;

import com.lms.LibraryManagementSystem.Entities.OTP;
import com.lms.LibraryManagementSystem.Entities.Users;
import com.lms.LibraryManagementSystem.Repositories.OTPRepository;
import com.lms.LibraryManagementSystem.Repositories.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class Listener {
    @Autowired
    UserRepository userRepository;
    @Autowired
    OTPRepository otpRepository;
    @RabbitHandler
    @RabbitListener(queues = "otp-confirmation-queue")
    public void receiveConfirmationOTP(OTP otp){
        log.info("OTP Confirmation received from user "+otp.getUserId());

        OTP latestOTP = otpRepository.findLatestOTP(otp.getUserId());
        if(latestOTP.getOtp() == otp.getOtp()){
            Users user = userRepository.findById(otp.getUserId()).get();
            user.setIsRegistered(1);
            userRepository.save(user);
            log.info("User Account Confirmed!");
        }
        log.error("Wrong OTP!");

    }
}
