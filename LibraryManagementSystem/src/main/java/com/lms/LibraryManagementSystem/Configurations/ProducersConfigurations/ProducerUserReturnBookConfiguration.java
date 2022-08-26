package com.lms.LibraryManagementSystem.Configurations.ProducersConfigurations;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProducerUserReturnBookConfiguration {

    @Value("${book.return.queue.name}")
    private String queueName;

    @Value("${book.return.exchange.name}")
    private String exchange;

    @Value("${book.return.routing.key}")
    private String routingKey;

    @Bean("book-return-queue")
    public Queue queue() {
        return new Queue(queueName);
    }

    @Bean("book-return-exchange")
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    @Bean("book-return-queue-binding")
    public Binding binding() {
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(routingKey);
    }


}
