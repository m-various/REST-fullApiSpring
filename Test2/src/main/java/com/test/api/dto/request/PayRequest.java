package com.test.api.dto.request;

import java.util.List;
import java.util.UUID;

public class PayRequest {
    private UUID userId;
    //private UUID productId;
    private List<UUID> productListId;

    public List<UUID> getProductListId() {
        return productListId;
    }

    public void setProductListId(List<UUID> productListId) {
        this.productListId = productListId;
    }

    public PayRequest(UUID userId, List<UUID>productListId) {
        this.userId = userId;
        this.productListId = productListId;
    }

    public PayRequest() {
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }


}
