package com.lms.LibraryManagementSystem.Producers;


import com.lms.LibraryManagementSystem.Entities.OTP;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class ProducerOTP {

    @Value("${otp.exchange.name}")
    private String exchange;

    @Value("${otp.routing.key}")
    private String routingKey;

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendJsonMessage(OTP otp) {
        log.info(String.format("Json message sent -> %s", otp));
        amqpTemplate.convertAndSend(exchange, routingKey, otp);
    }




}