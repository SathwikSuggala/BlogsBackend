package com.Blogs.Backend.BlogsBackend.Security.repository;


import com.Blogs.Backend.BlogsBackend.Security.entity.Seller;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellerRepository extends MongoRepository<Seller,String> {

    @Query("{ 'userName': ?0 }, { '$push': { 'productId': ?1 } }")
    void addProductToSeller(String username, String productId);

    Seller findByUserName(String userName);

    List<Seller> findAllByUserName(List<String> sellers);

    List<Seller> findAllByUserNameIn(List<String> sellerUserNames);

    void deleteByUserName(String userName);

}
