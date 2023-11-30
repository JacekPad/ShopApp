package com.pad.app.factories.messagetemplate;

import com.pad.app.model.messageTemplates.MessageTemplate;
import com.pad.app.model.messageTemplates.OrderMessageTemplate;
import com.pad.app.swagger.model.Order;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderMessageFactoryAbstract implements AbstractTemplateFactory {

    private final Order order;

    @Override
    public MessageTemplate createTemplate() {
        return new OrderMessageTemplate(order);
    }
}
