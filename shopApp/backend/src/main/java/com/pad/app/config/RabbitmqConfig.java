package com.pad.app.config;

import com.pad.app.model.messageTemplates.OrderMessageTemplate;
import com.pad.app.model.messageTemplates.ProductQuantityChangeMessageTemplate;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitmqConfig {

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.template.exchange}")
    private String exchangeName;

    @Value("${spring.rabbitmq.template.queue.sendOrder}")
    private String sendOrderQueue;

    @Value("${spring.rabbitmq.template.routing-key.sendOrder}")
    private String sendOrderRoutingKey;

    @Value("${spring.rabbitmq.template.queue.productCountChange}")
    private String productCountChangeQueue;

    @Value("${spring.rabbitmq.template.routing-key.productCountChange}")
    private String productCountChangeRoutingKey;

    @Bean
    CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        return cachingConnectionFactory;
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();

//        mapper if consumer object have different package paths
        DefaultJackson2JavaTypeMapper mapper = new DefaultJackson2JavaTypeMapper();
        mapper.setIdClassMapping(Map.of("productQuantity", ProductQuantityChangeMessageTemplate.class, "makeOrder", OrderMessageTemplate.class));
//        set trusted packages for consumer without custom classMappings
//        mapper.setTrustedPackages("*");
        jackson2JsonMessageConverter.setClassMapper(mapper);

        return jackson2JsonMessageConverter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public Queue productQuantityChangeQueue() {
        return new Queue(productCountChangeQueue);
    }

    @Bean
    public Queue sendOrderQueue() {
        return new Queue(sendOrderQueue);
    }

    @Bean
    DirectExchange exchange() {
        return ExchangeBuilder.directExchange(exchangeName).durable(true).build();
    }

    @Bean
    Binding productCountChangeBinding() {
        return BindingBuilder.bind(productQuantityChangeQueue())
                .to(exchange()).with(productCountChangeRoutingKey);
    }

    @Bean
    Binding sendOrderBinding() {
        return BindingBuilder.bind(sendOrderQueue())
                .to(exchange()).with(sendOrderRoutingKey);
    }

}
