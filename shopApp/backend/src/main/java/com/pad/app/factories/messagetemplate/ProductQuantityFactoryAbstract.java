package com.pad.app.factories.messagetemplate;

import com.pad.app.model.messageTemplates.MessageTemplate;
import com.pad.app.model.messageTemplates.ProductQuantityChangeMessageTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
public class ProductQuantityFactoryAbstract implements AbstractTemplateFactory {

    private final int quantity;
    private final long productId;

    @Override
    public MessageTemplate createTemplate() {
        return new ProductQuantityChangeMessageTemplate(quantity, productId);
    }
}
