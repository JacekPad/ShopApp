package com.pad.orderapp.model.DTO;

import com.pad.orderapp.model.enums.MessageType;
import com.pad.orderapp.swagger.model.Order;
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
