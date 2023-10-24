package com.pad.app.model.messageTemplates;

import com.pad.app.model.enums.MessageType;
import com.pad.app.swagger.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class OrderMessageTemplate extends MessageTemplate {

    private Order order;
    public OrderMessageTemplate() {
        super(MessageType.SEND_ORDER);
    }
}
