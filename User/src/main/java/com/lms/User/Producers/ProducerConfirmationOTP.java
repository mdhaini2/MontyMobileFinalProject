package com.lms.User.Producers;

import com.lms.User.Entities.OTP;
import com.lms.User.Producers.DataProducer.ConfirmationOTP;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class ProducerConfirmationOTP {

    @Value("${otp-confirmation.exchange.name}")
    private String exchange;

    @Value("${otp-confirmation.routing.key}")
    private String routingKey;

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendJsonMessage(OTP otp) {
        log.info(String.format("User message sent -> %s", otp));
        amqpTemplate.convertAndSend(exchange, routingKey, otp);
    }




}