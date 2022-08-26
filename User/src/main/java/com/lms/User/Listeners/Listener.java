package com.lms.User.Listeners;

import com.lms.User.Entities.OTP;
import com.lms.User.Entities.UserReturnBookMessage;
import com.lms.User.Producers.ProducerConfirmationOTP;
import com.lms.User.Services.ReturnBookService;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.time.Duration;

@Log4j2
@Component
public class Listener {
    public static final String HASH_KEY = "user-";
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    ProducerConfirmationOTP producerConfirmationOTP;
    @Autowired
    ReturnBookService returnBookService;

    @RabbitListener(queues = "otp-queue")
    @RabbitHandler
    public void receiveOTP(OTP otp) throws IOException {
        log.info("OTP received from server "+ otp.toString());

        redisTemplate.opsForValue().set(HASH_KEY + otp.getUserId(), otp.getOtp(), Duration.ofSeconds(300));
        log.info("OTP stored in redis");
//        producerConfirmationOTP.sendJsonMessage(otp);
//        log.info("OTP sent to server");
    }

    @RabbitListener(queues = "book-return-queue")
    @RabbitHandler
    public void receiveBookReturn(UserReturnBookMessage userReturnBookMessage) throws ParseException {
        log.info("Book return confirmation received"+ userReturnBookMessage.toString());
        returnBookService.userReturnBook(userReturnBookMessage);
    }


}
