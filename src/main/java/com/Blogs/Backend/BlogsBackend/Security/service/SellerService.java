package com.Blogs.Backend.BlogsBackend.Security.service;

import com.Blogs.Backend.BlogsBackend.Security.entity.Blog;
import com.Blogs.Backend.BlogsBackend.Security.entity.BlogRequest;
import com.Blogs.Backend.BlogsBackend.Security.entity.Seller;
import com.Blogs.Backend.BlogsBackend.Security.repository.BlogRepository;
import com.Blogs.Backend.BlogsBackend.Security.repository.BlogRequestRepository;
import com.Blogs.Backend.BlogsBackend.Security.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerService {
    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    BlogRepository blogRepository;
    @Autowired
    BlogRequestRepository blogRequestRepository;

    public void addMyBlog(BlogRequest blog, String name) {

        blogRequestRepository.save(blog);
    }

    public List<Blog> gtMyBlogs(String name) {

        Seller seller = sellerRepository.findByUserName(name);
        List<Blog> blogs = blogRepository.findAllById(seller.getBlogId());
        return blogs;
    }

    public void updateMyBlog(Blog blog) {
        blogRepository.save(blog);
    }

    public void deleteBlog(String blogId, String name) {
        blogRepository.deleteById(blogId);
        Seller seller = sellerRepository.findByUserName(name);
        seller.getBlogId().removeIf(i -> i.equals(blogId));
        sellerRepository.save(seller);
    }
}
