package com.Blogs.Backend.BlogsBackend.Security.repository;

import com.Blogs.Backend.BlogsBackend.Security.entity.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends MongoRepository<Customer,String> {
    Customer findByUserName(String name);

    void deleteByUserName(String userName);

}
