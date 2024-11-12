package com.Blogs.Backend.BlogsBackend.Security.service;


import com.Blogs.Backend.BlogsBackend.Security.entity.User;
import com.Blogs.Backend.BlogsBackend.Security.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserService implements UserDetailsService {

    @Autowired
    private UserDetailsRepository repository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        System.out.println("accessing dao\n\n");
        User user = repository.findByUsername(userName).orElse(null);
        if(user == null){
            throw new IllegalArgumentException("this token's use has been deleted.");
        }
        return new MyUserDetails(user.getRole().toString(), user.getUserName(), user.getPassword());
    }

}
