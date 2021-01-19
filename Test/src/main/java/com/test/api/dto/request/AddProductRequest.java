package com.test.api.dto.request;

import com.test.api.dto.RequestBase;
import com.test.db.products.ProductCategory;

public class AddProductRequest extends RequestBase {
    private String productName;
    private ProductCategory productCategory;
    private Long productPrice;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public Long getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Long productPrice) {
        this.productPrice = productPrice;
    }
}

