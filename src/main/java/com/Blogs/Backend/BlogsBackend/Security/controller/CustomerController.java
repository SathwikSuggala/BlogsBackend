package com.Blogs.Backend.BlogsBackend.Security.controller;

import com.Blogs.Backend.BlogsBackend.Security.entity.Blog;
import com.Blogs.Backend.BlogsBackend.Security.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
@CrossOrigin
public class CustomerController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/getAllBlogs")
    public List<Blog> getAllBlogs(){
        return blogService.getAllBlogs();
    }

}
