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
public class BlogRequestService {
    @Autowired
    private BlogRequestRepository blogRequestRepository;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private SellerRepository sellerRepository;

    public void approveBlog(String data) {
        BlogRequest blogRequest = blogRequestRepository.findById(data).orElse(null);
        Blog blog = new Blog();
        assert blogRequest != null;
        blog.setBlog(blogRequest);
        String name = blog.getCreatedBy();
        Seller seller = sellerRepository.findByUserName(name);
        blog.setCreatedBy(name);
        blogRepository.save(blog);
        blog = blogRepository.findByTitleAndCreatedBy(blog.getTitle(), name).orElse(null);
        seller.getBlogId().add(blog.getId());
        sellerRepository.save(seller);
    }

    public void rejectBlog(String data) {
        blogRequestRepository.deleteById(data);
    }

    public List<BlogRequest> getAllBlogRequests() {
        return blogRequestRepository.findAll();
    }
}
