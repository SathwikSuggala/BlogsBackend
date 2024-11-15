package com.Blogs.Backend.BlogsBackend.Security.controller;

import com.Blogs.Backend.BlogsBackend.Security.entity.Blog;
import com.Blogs.Backend.BlogsBackend.Security.entity.BlogRequest;
import com.Blogs.Backend.BlogsBackend.Security.entity.Seller;
import com.Blogs.Backend.BlogsBackend.Security.entity.User;
import com.Blogs.Backend.BlogsBackend.Security.repository.SellerRepository;
import com.Blogs.Backend.BlogsBackend.Security.repository.UserRepository;
import com.Blogs.Backend.BlogsBackend.Security.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/seller")
public class SellerController {
    @Autowired
    private SellerService sellerService;

    @Autowired
    private SellerRepository sellerRepo;

    @PostMapping("/addBlog")
    public void addMyBlog(@RequestBody BlogRequest blog, Principal principal) {
        sellerService.addMyBlog(blog, principal.getName());
    }

    @GetMapping("/getMyBlogs")
    public List<Blog> getMyBlogs(Principal principal){
        return sellerService.gtMyBlogs(principal.getName());
    }

    @PutMapping("/updateBlog/{blogId}")
    public Blog updateBlog(@RequestBody Blog blog, @PathVariable String blogId){

        return sellerService.updateMyBlog(blogId, blog);
    }

    @DeleteMapping("/deleteBlog/{blogId}")
    public void deleteBlog(@PathVariable String blogId, Principal principal){
        sellerService.deleteBlog(blogId, principal.getName());
    }
}
