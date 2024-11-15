package com.Blogs.Backend.BlogsBackend.Security.repository;


import com.Blogs.Backend.BlogsBackend.Security.entity.RejectSellerRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RejectedSellerRequestRepository extends MongoRepository<RejectSellerRequest, String> {

    Optional<Object> findByUserName(String userName);

}
