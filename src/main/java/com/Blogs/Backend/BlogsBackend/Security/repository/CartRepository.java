package com.Blogs.Backend.BlogsBackend.Security.repository;


import com.Blogs.Backend.BlogsBackend.Security.entity.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {
    Optional<Cart> findByUserName(String name);

    void deleteByUserName(String userName);

}
