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
import java.util.Optional;

@Service
public class SellerService {
    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    BlogRepository blogRepository;
    @Autowired
    BlogRequestRepository blogRequestRepository;

    public void addMyBlog(BlogRequest blogRequest, String name) {

        Blog blog = new Blog();
        blog.setBlog(blogRequest);
        Seller seller = sellerRepository.findByUserName(name);
        blog.setCreatedBy(name);
        blogRepository.save(blog);
        blog = blogRepository.findByTitleAndCreatedBy(blog.getTitle(), name).orElse(null);
        seller.getBlogId().add(blog.getId());
        sellerRepository.save(seller);

    }

    public List<Blog> gtMyBlogs(String name) {

        Seller seller = sellerRepository.findByUserName(name);
        List<Blog> blogs = blogRepository.findAllById(seller.getBlogId());
        return blogs;
    }

    public Blog updateMyBlog(String blogId, Blog blog) {
        // Fetch the blog from the repository using the provided ID
        Blog existingBlog = blogRepository.findById(blogId).orElse(null);

        if (existingBlog != null) {
            // Blog exists, so update it with the new values
            if(blog.getTitle()!=null){
                existingBlog.setTitle(blog.getTitle());
            }
            if(blog.getDescription()!=null){
                existingBlog.setDescription(blog.getDescription());
            }
            if(blog.getImageUrl()!=null){
                existingBlog.setImageUrl(blog.getImageUrl());
            }
            // Save the updated blog
            blogRepository.save(existingBlog);
            return existingBlog;
        } else {
            // If the blog doesn't exist, handle it (e.g., throw an exception or return an error)
            throw new RuntimeException("Blog with ID " + blogId + " not found.");
        }

    }

    public void deleteBlog(String blogId, String name) {
        blogRepository.deleteById(blogId);
        Seller seller = sellerRepository.findByUserName(name);
        seller.getBlogId().removeIf(i -> i.equals(blogId));
        sellerRepository.save(seller);
    }
}
