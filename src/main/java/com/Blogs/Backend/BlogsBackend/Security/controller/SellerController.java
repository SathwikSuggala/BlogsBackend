package com.Blogs.Backend.BlogsBackend.Security.controller;

import com.Blogs.Backend.BlogsBackend.Security.entity.Blog;
import com.Blogs.Backend.BlogsBackend.Security.entity.BlogRequest;
import com.Blogs.Backend.BlogsBackend.Security.entity.Seller;
import com.Blogs.Backend.BlogsBackend.Security.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/seller")
public class SellerController {
    @Autowired
    private SellerService sellerService;

    @PostMapping("/addBlog")
    public void addMyBlog(@RequestBody BlogRequest blog, Principal principal) {
        sellerService.addMyBlog(blog, principal.getName());
    }

    @GetMapping("/getMyBlogs")
    public List<Blog> getMyBlogs(Principal principal){
        return sellerService.gtMyBlogs(principal.getName());
    }

    @PutMapping("/updateBlog")
    public void updateBlog(@RequestBody Blog blog){
        sellerService.updateMyBlog(blog);
    }

    @DeleteMapping("/deleteBlog/{blogId}")
    public void deleteBlog(@PathVariable String blogId, Principal principal){
        sellerService.deleteBlog(blogId, principal.getName());
    }
}
