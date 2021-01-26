package com.test.db.repo;

import com.test.db.products.Product;
import com.test.db.products.ProductCategory;
import com.test.db.products.UserProduct;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserProductRepo extends CrudRepository<UserProduct, UUID> {

}
