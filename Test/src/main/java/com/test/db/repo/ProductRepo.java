package com.test.db.repo;

import com.test.db.products.Product;
import com.test.db.products.ProductCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductRepo extends CrudRepository<Product, UUID> {
    List<Product> findAllByCategory(@Param("category") ProductCategory category);
    //Product findByID (@Param("id") UUID id);
}
