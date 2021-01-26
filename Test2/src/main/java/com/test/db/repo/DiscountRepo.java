package com.test.db.repo;

import com.test.db.Account;
import com.test.db.Discount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface DiscountRepo extends CrudRepository<Discount, UUID> {
    Discount findByProductId(@Param("productId") UUID productId);
}
