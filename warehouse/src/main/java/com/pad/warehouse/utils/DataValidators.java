package com.pad.warehouse.utils;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.pad.warehouse.service.ProductCacheService;
import com.pad.warehouse.swagger.model.Product;
import com.pad.warehouse.swagger.model.ProductDescription;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataValidators {
    private final ProductCacheService cacheService;

    public boolean validateProduct(Product product, Map<String, String> errors, boolean isAddMode) {
        log.info("validate product: {} START", product);

        if (isAddMode && product.getName() == null) errors.put("name", "name cannot be empty");

        if (isAddMode && product.getProductCode() == null) errors.put("productCode", "Product code cannot be empty");

        if (isAddMode && product.getQuantity() == null) {
            errors.put("quantity", "product's quantity cannot be empty");
        } else if (product.getQuantity() != null) {
            try {
                int quantityInt = Integer.parseInt(product.getQuantity());
                if (quantityInt <= 0) errors.put("quantity", "quantity must be a postive integer");
            } catch (NumberFormatException ex) {
                errors.put("quantity", "quantity must be a valid integer");
            }
        }

        if (isAddMode && product.getPrice() == null) {
            errors.put("price", "product's price cannot be empty");
        } else if (product.getPrice() != null) {
            try {
                float priceFloat = Float.parseFloat(product.getPrice());
                if (priceFloat <= 0) errors.put("price", "price must be a positive number");
            } catch (NumberFormatException ex) {
                errors.put("price", "price must be a valid number");
            }
        }

        if (isAddMode && product.getStatus() == null) {
            errors.put("status", "status cannot be empty");
        } else if (product.getStatus() != null) {
            Map<String, String> statusMap = cacheService.getStatusMap();
            if (!statusMap.containsKey(product.getStatus())) errors.put("status", "incorrect status code");
        }

        if (isAddMode && product.getType() == null) {
            errors.put("type", "type cannot be empty");
        } else if (product.getType() != null) {
            Map<String, String> typeMap = cacheService.getTypeMap();
            log.error("typeMap: {}", typeMap);
            if (!typeMap.containsKey(product.getType())) errors.put("type", "incorrect type code");            
        }

        if (isAddMode && product.getSubtype() == null) {
            errors.put("subtype", "subtype cannot be empty");
        } else if (product.getSubtype() != null) {
            Map<String, String> subtypeMap = cacheService.getSubtypeMap();
            if (!subtypeMap.containsKey(product.getSubtype())) errors.put("subtype", "incorrect subtype code");            
        }
        log.info("validate product: {} END", product);
        return errors.isEmpty();
    }

    public boolean validateProductDescription(ProductDescription productDescription, Map<String, String> errors, boolean isAddMode) {
        log.info("validate product description: {} START", productDescription);
        if (isAddMode && productDescription.getProductId() == null) errors.put("product", "Product ID cannot be empty");
        if (isAddMode && productDescription.getProductDescription() == null) errors.put("description", "product description cannot be empty");
        log.info("validate product description: {} END", productDescription);
        return errors.isEmpty();
    }
}
