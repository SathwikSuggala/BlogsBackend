package com.Blogs.Backend.BlogsBackend.Security.repository;

import com.Blogs.Backend.BlogsBackend.Security.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product,String> {

    void deleteByProductNameAndCreatedBy(String productName, String createdBy);

    @Aggregation(pipeline = {
            "{ $match: { createdBy: ?0 } }"
    })
    List<Product> getMyProducts(String name);

    Optional<Product> findByProductNameAndCreatedBy(String productName, String userName);

    @Query("{ 'productName': { $regex: ?0, $options: 'i' } }")
    List<Product> searchByProductNamePartial(String productName);

    @Query("{ 'productName': { $regex: ?0, $options: 'i' } }")
    Page<Product> findByProductNameContaining(String productName, Pageable pageable);


    Page<Product> findByCategory(String category, Pageable pageable);

}
