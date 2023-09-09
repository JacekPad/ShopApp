package com.pad.app.service;

import com.pad.app.model.ProductOrder;
import com.pad.warehouse.swagger.model.Product;
import com.pad.warehouse.swagger.model.ProductList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ManageProductService manageProductService;

    public List<ProductList> getProducts() {
//        TODO cache caffeine it
        return manageProductService.getProducts();
    }

    public ProductList getProductDetails(String productId) {
        List<ProductList> products = getProducts();
        Optional<ProductList> product = products.stream().filter(productList -> productList.getProduct().getId().equals(productId)).findFirst();
        //            some errors
        return product.orElse(null);
    }

    public boolean isProductAvailable(ProductOrder productOrder) {
        Product orderProduct = productOrder.getProduct();
        List<ProductList> products = getProducts();

        Optional<ProductList> product = products.stream().filter(productList -> productList.getProduct().getId().equals(orderProduct.getId())).findFirst();

        if (product.isPresent()) {
            int productQuantity = Integer.parseInt(product.get().getProduct().getQuantity());
            int quantityBought = productOrder.getQuantityBought();
            return productQuantity - quantityBought >= 0;
        } else {
//            some errors that product doesn't exist?
            return false;
        }
    }

    public void updateProductAvailability(String productId, int quantityChange) {
        manageProductService.updateProductCount(productId, quantityChange);
    }
}
