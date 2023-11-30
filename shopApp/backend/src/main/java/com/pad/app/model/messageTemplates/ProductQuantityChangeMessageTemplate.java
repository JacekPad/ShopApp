package com.pad.app.model.messageTemplates;

import com.pad.app.model.enums.MessageType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProductQuantityChangeMessageTemplate extends MessageTemplate {

    private int quantity;
    private Long productId;

    public ProductQuantityChangeMessageTemplate(int quantity, Long productId) {
        super(MessageType.CHANGE_PRODUCT_COUNT);
        this.productId = productId;
        this.quantity = quantity;
    }
    public ProductQuantityChangeMessageTemplate() {
        super(MessageType.CHANGE_PRODUCT_COUNT);
    }
}
