package com.test.api.dto.request;

import com.test.api.dto.RequestBase;

import java.util.UUID;

public class AddMoneyRequest extends RequestBase {
    private UUID userId;
    private Long amount;

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
