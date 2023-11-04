package com.pad.orderapp.model.DTO;

import com.pad.orderapp.model.enums.MessageType;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class MessageTemplate {

    public MessageTemplate() {
        this.timestamp = LocalDateTime.now().toString();
        this.messageId = UUID.randomUUID().toString();
    }

    public MessageTemplate(MessageType messageType) {
        this();
        this.messageType = messageType;
    }

    private final String messageId;
    private final String timestamp;
    private MessageType messageType;
}
