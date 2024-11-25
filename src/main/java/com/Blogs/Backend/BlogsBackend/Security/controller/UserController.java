package com.Blogs.Backend.BlogsBackend.Security.controller;


import com.Blogs.Backend.BlogsBackend.Security.dto.*;
import com.Blogs.Backend.BlogsBackend.Security.entity.Blog;
import com.Blogs.Backend.BlogsBackend.Security.entity.SellerRequest;
import com.Blogs.Backend.BlogsBackend.Security.jwt.service.JwtService;
import com.Blogs.Backend.BlogsBackend.Security.service.BlogService;
import com.Blogs.Backend.BlogsBackend.Security.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private BlogService blogService;


    //registration for customer
    @PostMapping("/register")
    public String resisterDummy(@RequestBody UserDto user){

        userService.addCustomer(user);
        return "Success";
    }

    //login api
    @PostMapping("/authenticate")
    public ResponseEntity<?> generateToken(@RequestBody AuthRequestDto authRequestDto, HttpServletResponse response){

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.getUserName(), authRequestDto.getPassword()));
        if(authentication.isAuthenticated()) {

            String role = userService.getUserRole(authRequestDto.getUserName());
            String jwt = jwtService.generateToken(authRequestDto.getUserName(), role);
            jwtService.addTokenToResponse(response, jwt, role);
            return new ResponseEntity<>(new LoginResponseDto(jwt, role), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
        }
    }

    //seller registration
    @PostMapping("/sellers/register")
    public ResponseEntity<?> sellerRegistration(@RequestBody UserDto user){

        try{
            RegisterResponseDto dto = userService.addSeller(user);;
            if(dto.isStatus()) {
                return new ResponseEntity<>(dto, HttpStatus.OK);
            }else {
                return new ResponseEntity<>(dto, HttpStatus.CONFLICT);
            }
        }
        catch (IllegalArgumentException iae){
            return new ResponseEntity<>(iae.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    //to get account details
    @PostMapping("/getMyAccount")
    public ResponseEntity<?> getMyAccount(Principal principal){
        try{
            return new ResponseEntity<>(userService.getMyAccount(principal.getName()), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("duplicate username found or account has been already deleted", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //to update the account
    @PutMapping("/updateAccount")
    public ResponseEntity<?> updateAccount(@RequestBody UserDto userDto, Principal principal){
        try {
            userService.updateAccount(userDto, principal.getName());
            return new ResponseEntity<>(true, HttpStatus.OK);
        }catch (IllegalArgumentException iae){
            return new ResponseEntity<>(false,HttpStatus.CONFLICT);
        }
    }

    //to delete his account
    @DeleteMapping("/deleteAccount")
    public ResponseEntity<String> deleteAccount(HttpServletRequest request){
        String token = null;
        String userName = null;
        String role = null;
        String header = request.getHeader("Authorization");
        //extracting role and userName from the request header.
        if( null != header && header.startsWith("Bearer ")){
            token = header.substring(7);
            userName = jwtService.extractUserName(token);
            role = jwtService.extractRole(token);
        }
        userService.deleteAccount(userName, role);
        return new ResponseEntity<>(userName,HttpStatus.OK);
    }

    @GetMapping("/getAllBlogs")
    public List<Blog> getAllBlogs(){
        return blogService.getAllBlogs();
    }

    @GetMapping("/getBlog/{blogId}")
    public Blog getBlog(@PathVariable String blogId){
        return blogService.getBlog(blogId);
    }
}
