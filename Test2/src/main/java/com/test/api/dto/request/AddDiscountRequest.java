package com.test.api.dto.request;

import java.util.UUID;

public class AddDiscountRequest {
    private UUID productId;
    private Integer discount;

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }
}
