package com.Blogs.Backend.BlogsBackend.Security.repository;

import com.Blogs.Backend.BlogsBackend.Security.entity.Blog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends MongoRepository<Blog, String> {
}
