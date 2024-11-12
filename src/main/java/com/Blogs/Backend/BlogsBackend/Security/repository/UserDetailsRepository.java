package com.Blogs.Backend.BlogsBackend.Security.repository;


import com.Blogs.Backend.BlogsBackend.Security.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepository extends MongoRepository<User, String> {
    @Query("{ 'userName' : ?0 }")  // MongoDB query syntax, ?0 refers to the first method parameter
    Optional<User> findByUsername(String userName);
}
