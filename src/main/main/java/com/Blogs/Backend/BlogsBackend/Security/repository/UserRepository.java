package com.Blogs.Backend.BlogsBackend.Security.repository;


import com.Blogs.Backend.BlogsBackend.Security.entity.Seller;
import com.Blogs.Backend.BlogsBackend.Security.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    @Query("{ 'userName' : ?0 }")
    Optional<User> findByUsername(String userName);

    void deleteByUserName(String userName);

    List<Seller> findAllByUserName(List<String> sellers);

    List<User> findAllByUserNameIn(List<String> sellers);

}
