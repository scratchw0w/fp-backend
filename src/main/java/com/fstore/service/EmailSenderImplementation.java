package com.fstore.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fstore.model.EmailRequest;
import com.fstore.properties.KafkaProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
public class EmailSenderImplementation implements EmailSender {
    private final ObjectMapper mapper;
    private final KafkaProperties properties;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void send(EmailRequest request) {
        try {
            String body = mapper.writeValueAsString(request);
            kafkaTemplate.send(properties.emailTopic(), body);
        } catch (JsonProcessingException exception) {
            throw new RuntimeException("During converting body, an exception occurred", exception);
        }
    }
}
