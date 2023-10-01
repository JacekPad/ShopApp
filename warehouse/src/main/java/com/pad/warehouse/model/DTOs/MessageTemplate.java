package com.pad.warehouse.model.DTOs;

import com.pad.warehouse.model.enums.MessageType;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
public abstract class MessageTemplate implements Serializable {

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

