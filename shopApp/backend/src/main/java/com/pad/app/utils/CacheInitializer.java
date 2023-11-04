package com.pad.app.utils;

import com.pad.app.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CacheInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final ProductService productService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        productService.updateProductCache();
    }
}
