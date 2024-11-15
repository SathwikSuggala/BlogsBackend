package com.Blogs.Backend.BlogsBackend.Security.repository;


import com.Blogs.Backend.BlogsBackend.Security.entity.SellerRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SellerRequestRepository extends MongoRepository<SellerRequest, String> {

    Optional<SellerRequest> findByUserName(String userName);

    void deleteByUserName(String data);

}
