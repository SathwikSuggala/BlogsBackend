package com.Blogs.Backend.BlogsBackend.Security.controller;


import com.Blogs.Backend.BlogsBackend.Security.dto.RequestStringDto;
import com.Blogs.Backend.BlogsBackend.Security.entity.BlogRequest;
import com.Blogs.Backend.BlogsBackend.Security.exceptions.NoContentException;
import com.Blogs.Backend.BlogsBackend.Security.service.AdminService;
import com.Blogs.Backend.BlogsBackend.Security.service.BlogRequestService;
import com.Blogs.Backend.BlogsBackend.Security.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private BlogRequestService blogRequestService;

    @PostMapping("/getAllRequests")
    public ResponseEntity<?> getAllRequests(){
        try{
            return new ResponseEntity<>(adminService.getAllRequests(), HttpStatus.OK);
        }
        catch (NoContentException ne){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/approveRequest")
    public ResponseEntity<?> approveRequest(@RequestBody RequestStringDto requestStringDto){
        System.out.println(requestStringDto.getData());
        try {
            return new ResponseEntity<>(adminService.approveRequest(requestStringDto), HttpStatus.OK);
        }catch (IllegalArgumentException ie){
            return new ResponseEntity<>(ie.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/rejectRequest")
    public ResponseEntity<?> rejectRequest(@RequestBody RequestStringDto requestStringDto){
        try {
            return new ResponseEntity<>(adminService.rejectRequest(requestStringDto), HttpStatus.OK);
        }catch (IllegalArgumentException ie){
            return new ResponseEntity<>(ie.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/getAllRejectedRequests")
    public ResponseEntity<?> getAllRejectedSellerRequests(){
        try{
            return new ResponseEntity<>(adminService.getAllRejectedRequests(), HttpStatus.OK);
        }
        catch (NoContentException ne){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/getRejectedRequest")
    public ResponseEntity<?> getRejectedRequest(@RequestBody RequestStringDto dto){
        try {
            return new ResponseEntity<>(adminService.getRejectedRequest(dto.getData()), HttpStatus.OK);
        }catch (IllegalArgumentException ie){
            return new ResponseEntity<>(ie.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers(){
        return new ResponseEntity<>(adminService.getAllUsersAccount(),HttpStatus.OK);
    }

    @DeleteMapping("/deleteBlog/{blogId}")
    public void deleteBlog(@PathVariable String blogId){
        blogService.deleteBlog(blogId);
    }

    @DeleteMapping("/deleteUser/{userName}")
    public void deleteUser(@PathVariable String userName){
        adminService.deleteUser(userName);
    }

    @GetMapping("/getAllBlogRequests")
    public List<BlogRequest> getAllBlogRequests(){
        return blogRequestService.getAllBlogRequests();
    }

    @PostMapping("/approveBlog")
    public void approveBlog(@RequestBody RequestStringDto data){
        blogRequestService.approveBlog(data.getData());
    }

    @PostMapping("/rejectBlog")
    public void rejectBlog(@RequestBody RequestStringDto data){
        blogRequestService.rejectBlog(data.getData());
    }
}
