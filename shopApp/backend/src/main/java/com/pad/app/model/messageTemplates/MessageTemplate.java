package com.pad.app.model.messageTemplates;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MessageTemplate<T> {

    public MessageTemplate() {
        this.timestamp = LocalDateTime.now().toString();
        this.messageId = UUID.randomUUID().toString();
    }

    @Setter(AccessLevel.NONE)
    private String messageId;

    @Setter(AccessLevel.NONE)
    private String timestamp;

    private T messegeContent;

}
