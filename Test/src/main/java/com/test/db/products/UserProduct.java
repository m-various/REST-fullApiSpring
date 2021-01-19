package com.test.db.products;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.istack.NotNull;
import com.test.db.Account;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "UserProduct")
public class UserProduct {
    @Id
    @NotNull
    @Column(unique=true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name="userId")
    private UUID userId;
    @Column(name="productId")
    private UUID productId;

    @OneToOne
    @JoinColumn(name = "userId",insertable = false,updatable = false)
    @JsonIgnore
    private Account account;
    @OneToOne
    @JoinColumn(name = "productId",insertable = false,updatable = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Product product;

    private Integer amount;

    public UserProduct() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public UserProduct(UUID userId, UUID productId, Integer amount) {
        this.userId = userId;
        this.productId = productId;
        this.amount = amount;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
