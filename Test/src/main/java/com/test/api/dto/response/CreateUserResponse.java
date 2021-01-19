package com.test.api.dto.response;

import com.test.api.dto.ResponseBase;
import com.test.db.Account;


public class CreateUserResponse extends ResponseBase {
    private Account user;

    public CreateUserResponse(Account user) {
        this.user = user;
    }

    public CreateUserResponse(String message) {
        super(message);
    }


    public Account getUser() {
        return user;
    }

    public void setUser(Account user) {
        this.user = user;
    }
}
