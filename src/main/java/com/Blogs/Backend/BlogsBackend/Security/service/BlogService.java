package com.Blogs.Backend.BlogsBackend.Security.service;

import com.Blogs.Backend.BlogsBackend.Security.entity.Blog;
import com.Blogs.Backend.BlogsBackend.Security.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {
    @Autowired
    private BlogRepository blogRepository;

    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }
}
