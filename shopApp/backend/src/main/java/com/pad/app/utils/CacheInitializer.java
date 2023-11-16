package com.pad.app.utils;

import com.pad.app.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class CacheInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final ProductService productService;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        productService.updateProductCache();
    }
}
