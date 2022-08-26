package com.lms.LibraryManagementSystem.Producers;

import com.lms.LibraryManagementSystem.Producers.DataProducer.UserReturnBookMessage;
import com.lms.LibraryManagementSystem.Repositories.OTPRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class ProducerUserReturnBook {
    @Value("${book.return.exchange.name}")
    private String exchange;

    @Value("${book.return.routing.key}")
    private String routingKey;
    @Autowired
    private OTPRepository otpRepository;
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendJsonMessage(UserReturnBookMessage userReturnBookMessage) {
        log.info(String.format("Json message sent -> %s", userReturnBookMessage));
        amqpTemplate.convertAndSend(exchange, routingKey, userReturnBookMessage);
    }



}
