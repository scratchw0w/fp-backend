package com.fstore.service;

import com.fstore.model.EmailRequest;

public interface EmailSender {
    void send(EmailRequest request);
}
