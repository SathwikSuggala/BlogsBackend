package com.Blogs.Backend.BlogsBackend.Security.repository;

import com.Blogs.Backend.BlogsBackend.Security.entity.Orders;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends MongoRepository<Orders, String> {
    List<Orders> findByUserName(String userName);

}
