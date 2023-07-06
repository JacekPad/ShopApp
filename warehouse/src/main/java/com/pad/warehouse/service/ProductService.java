package com.pad.warehouse.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.pad.warehouse.mappers.ProductMapper;
import com.pad.warehouse.model.entity.Product;
import com.pad.warehouse.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<Product> getProducts(String name, String productCode,
            String quantity, String price, String status, String type,
            String subtype, String created, String modified) {
                // TODO find better solution (this doesnt work)
                Product productParams = new Product();
                productParams.setName(name);
                productParams.setProductCode(productCode);
                if (quantity != null) {
                    productParams.setQuantity(Integer.parseInt(quantity));
                }
                if (price != null) {
                    productParams.setPrice(Long.parseLong(price));
                }
                productParams.setStatus(status);
                productParams.setType(type);
                productParams.setSubtype(subtype);

        return productRepository.findAll(Example.of(productParams));
    }

    public Product getProduct(String id) {
        return productRepository.findById(Long.parseLong(id)).get();
    }

}
