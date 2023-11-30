package com.pad.app.factories.messagetemplate;

import com.pad.app.model.messageTemplates.MessageTemplate;
import org.springframework.stereotype.Component;

@Component
public class TemplateFactory {

    public static MessageTemplate createTemplate(AbstractTemplateFactory factory) {
        return factory.createTemplate();
    }

}
