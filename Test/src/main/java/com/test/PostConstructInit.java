package com.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.db.products.Product;
import com.test.db.repo.ProductRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class PostConstructInit {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private ObjectMapper objectMapper = new ObjectMapper();

    private final ProductRepo productRepo;

    public PostConstructInit(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @PostConstruct
    public void importData() {
        try {
        logger.info("Import products started");
        List<Product> products = objectMapper.readValue(new ClassPathResource("products.json").getInputStream(), new TypeReference<List<Product>>() {});
        productRepo.saveAll(products);
        logger.info("Import products finished");

        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }
}
