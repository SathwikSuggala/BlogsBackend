package com.Blogs.Backend.BlogsBackend.Security.service;

import com.Blogs.Backend.BlogsBackend.Security.entity.Blog;
import com.Blogs.Backend.BlogsBackend.Security.entity.Seller;
import com.Blogs.Backend.BlogsBackend.Security.repository.BlogRepository;
import com.Blogs.Backend.BlogsBackend.Security.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private SellerRepository sellerRepository;

    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    public void deleteBlog(String blogId) {
        Blog blog = blogRepository.findById(blogId).orElse(null);
        if(blog == null){
            return;
        }
        Seller seller = sellerRepository.findByUserName(blog.getCreatedBy());
        seller.getBlogId().removeIf(i -> i.equals(blogId));
        sellerRepository.save(seller);
        blogRepository.deleteById(blogId);
    }

    public Blog getBlog(String blogId) {
        return blogRepository.findById(blogId).orElse(null);
    }
}
