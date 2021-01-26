package com.test.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ResponseBase {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    public ResponseBase(String message) {
        this.message = message;
    }

    public ResponseBase() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
