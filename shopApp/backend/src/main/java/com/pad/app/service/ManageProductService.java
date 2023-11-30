package com.pad.app.service;

import com.pad.app.exception.internal.FetchDataError;
import com.pad.app.exception.internal.SaveObjectException;
import com.pad.app.exception.notFound.NoObjectFound;
import com.pad.app.factories.messagetemplate.ProductQuantityFactory;
import com.pad.app.factories.messagetemplate.TemplateFactory;
import com.pad.app.model.messageTemplates.MessageTemplate;
import com.pad.app.service.webClient.WebClientService;
import com.pad.app.swagger.model.Product;
import com.pad.app.swagger.model.ProductsResponse;
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

//    private String PRODUCT_URI = "http://localhost:8080/products/test";
    public List<Product> fetchProducts() {
        log.info("fetchProducts - START");
        try {
            ProductsResponse productsResponse = webClientService.webClientGet(PRODUCT_URI, ProductsResponse.class);
            log.info("fetchProducts - END");
            return productsResponse.getProducts();
        } catch (Exception e) {
            log.error("Error fetching products: {}", e);
            throw new FetchDataError("Could not fetch products: " + e.getMessage());
        }
    }

    @Cacheable(value = "products", key = "#id")
    public Product getProduct(String id) {
        log.error("no object cached with id: {}", id);
        throw new NoObjectFound("No product in cache");
    }

    @CachePut(value = "products", key = "#product.getId()")
    @SuppressWarnings("UnusedReturnValue")
    public Product populateCache(Product product) {
        return product;
    }

    public void updateProductDatabase(String productId, int quantityChange) {
        log.info("updating product database - Service - START: {}", productId);
        try {
            MessageTemplate template = prepareProductTemplate(productId, quantityChange);
            workerService.prepareMessage(template);
            log.info("updating product database - Service - STOP");
        } catch (Exception e) {
            log.error("Error updating product database: {}", e);
            throw new SaveObjectException("Could not update product database" + e.getMessage());
        }
    }

    private MessageTemplate prepareProductTemplate(String productId, int quantityChange) {
        return TemplateFactory.createTemplate(new ProductQuantityFactory(quantityChange, Long.parseLong(productId)));
    }


}
