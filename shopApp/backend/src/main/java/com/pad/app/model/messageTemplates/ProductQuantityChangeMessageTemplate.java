package com.pad.app.model.messageTemplates;

import com.pad.app.model.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ProductQuantityChangeMessageTemplate extends MessageTemplate {

    private int quantity;
    private Long productId;

    public ProductQuantityChangeMessageTemplate() {
        super(MessageType.CHANGE_PRODUCT_COUNT);
    }
}
