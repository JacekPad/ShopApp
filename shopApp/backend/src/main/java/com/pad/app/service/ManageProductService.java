package com.pad.app.service;

import com.pad.app.model.messageTemplates.ProductQuantityChangeMessageTemplate;
import com.pad.warehouse.swagger.model.ProductList;
import com.pad.warehouse.swagger.model.ProductsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
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

    public List<ProductList> fetchProducts() {
        log.info("Cache empty: fetching items");
        ProductsResponse productsResponse = webClientService.webClientGet(PRODUCT_URI, ProductsResponse.class);
        return productsResponse.getProducts();
    }

    @Cacheable(value = "products", key = "#id")
    public ProductList getProduct(String id) {
//        TODO some error handling for no object in cache
        log.error("no object cached with id: {}", id);
        return null;
    }

    @CachePut(value = "products", key = "#productList.getProduct().getId()")
    public ProductList populateCache(ProductList productList) {
        log.info("populating cache with item: {}", productList);
        return productList;
    }

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
