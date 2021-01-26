package com.test.db;

import com.sun.istack.NotNull;
import com.test.db.products.UserProduct;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Accounts")
public class Account {
    @Id
    @NotNull
    @Column(unique=true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String firstName;
    private String lastName;
    private String phone;
    private Long money = 0L;

    public List<UserProduct> getProducts() {
        return products;
    }

    public void setProducts(List<UserProduct> products) {
        this.products = products;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId",insertable = false,updatable = false)
    private List<UserProduct> products;

    public Account(String firstName, String lastName, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public Account() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }
}
