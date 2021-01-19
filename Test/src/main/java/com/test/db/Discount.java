package com.test.db;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.istack.NotNull;
import com.test.db.products.Product;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "Discount")
public class Discount {
    @Id
    @NotNull
    @Column(unique=true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name="discount")
    private Integer discount;

    @Column(name="productId")
    private UUID productId;

    @OneToOne
    @JoinColumn(name = "productId",insertable = false,updatable = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Product product;

    public Discount(Integer discount, UUID productId) {
        this.discount = discount;
        this.productId = productId;
    }

    public Discount() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
