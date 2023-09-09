package com.pad.app.service;

import com.pad.app.model.messageTemplates.ProductQuantityChangeMessageTemplate;
import com.pad.warehouse.swagger.model.ProductList;
import com.pad.warehouse.swagger.model.ProductsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ManageProductService {

    private final WebClientService webClientService;

    private final WorkerService workerService;

    @Value("${product.get-all.uri}")
    private String PRODUCT_URI;

    @Cacheable("products")
    public List<ProductList> getProducts() {
        ProductsResponse productsResponse = webClientService.webClientGet(PRODUCT_URI, ProductsResponse.class);
        return productsResponse.getProducts();
    }


//    @CachePut(value = "products", key = "#productId")
    public void updateProductCount(String productId, int quantityChange) {
        log.info("updating product count: {}", productId);
        ProductQuantityChangeMessageTemplate template = prepareProductTemplate(productId, quantityChange);
        workerService.prepareMessage(template);
    }

    private ProductQuantityChangeMessageTemplate prepareProductTemplate(String productId, int quantityChange) {
        ProductQuantityChangeMessageTemplate template = new ProductQuantityChangeMessageTemplate();
        template.setProductId(Long.valueOf(productId));
        template.setQuantity(quantityChange);
        return template;
    }


}
