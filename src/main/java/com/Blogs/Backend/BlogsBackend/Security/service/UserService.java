package com.Blogs.Backend.BlogsBackend.Security.service;

import com.Blogs.Backend.BlogsBackend.Security.dto.RegisterResponseDto;
import com.Blogs.Backend.BlogsBackend.Security.enums.Role;
import com.Blogs.Backend.BlogsBackend.Security.dto.UserDto;
import com.Blogs.Backend.BlogsBackend.Security.entity.*;
import com.Blogs.Backend.BlogsBackend.Security.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private BlogRepository blogRepository;


    @Autowired
    private SellerRequestRepository sellerRequestRepository;

    @Autowired
    private RejectedSellerRequestRepository rejectedSellerRequestRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public RegisterResponseDto addSeller(UserDto userDto) {

        User user = new User();
        user.setUser(userDto);
        // Check if username already exists
        if (userRepository.findByUsername(user.getUserName()).isPresent() || sellerRequestRepository.findByUserName(user.getUserName()).isPresent()|| rejectedSellerRequestRepository.findByUserName(user.getUserName()).isPresent()) {
            return new RegisterResponseDto(user.getUserName(), user.getRole(),false);
        }
        // Encrypt the password
        String encryptedPassword = encoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        user.setRole(Role.SELLER);
        SellerRequest sellerRequest = new SellerRequest();
        sellerRequest.setUser(user);
        sellerRequestRepository.save(sellerRequest);
        return new RegisterResponseDto(user.getUserName(), user.getRole(),true);
    }

    public void updateAccount(UserDto userDto,String username){

        User user = new User();
        User tempUser = userRepository.findByUsername(username).orElse(null);
        user.setUser(userDto);
        user.updateUser(tempUser);
        System.out.println(tempUser.getRole());

        if(!user.getUserName().equals(username)){
            if(userRepository.findByUsername(user.getUserName()).isPresent())
            {
                throw new IllegalArgumentException("chose another user name");
            }
        }

        if(userDto.getPassword() != null) {
            String encryptedPassword = encoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);
        }
        user.setId(tempUser.getId());
        user.setRole(tempUser.getRole());

        userRepository.save(user);
    }

    public void deleteAccount(String userName, String role) {
        userRepository.deleteByUserName(userName);
    }



    //temporary
    public void addCustomer(UserDto userDto) {

        String userName = userDto.getUserName();
        //changing dto to entity
        User user = new User();
        user.setUser(userDto);
        String encryptedPassword = encoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        userRepository.save(user);

    }

    public void updateDummy(UserDto userDto,String username){

        User user = new User();
        User tempUser = userRepository.findByUsername(username).orElse(null);
        user.setUser(userDto);
        user.updateUser(tempUser);


        if(!user.getUserName().equals(username)){
            if(userRepository.findByUsername(user.getUserName()).isPresent() )
            {
                throw new IllegalArgumentException("chose another user name");
            }
        }

        if(userDto.getPassword() != null) {
            String encryptedPassword = encoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);
        }

        user.setId(tempUser.getId());
        user.setRole(tempUser.getRole());

        System.out.println(user);
        userRepository.save(user);
    }

    public String getUserRole(String userName) {

        User tempUser = userRepository.findByUsername(userName).orElse(null);
        return tempUser.getRole().toString();
    }

    public UserDto getMyAccount(String name) {
        User user = userRepository.findByUsername(name).orElse(null);
        UserDto userDto = new UserDto();
        userDto.setUserDto(user);
        return userDto;
    }
}
