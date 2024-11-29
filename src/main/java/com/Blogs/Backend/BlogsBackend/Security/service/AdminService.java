package com.Blogs.Backend.BlogsBackend.Security.service;


import com.Blogs.Backend.BlogsBackend.Security.dto.RequestStringDto;
import com.Blogs.Backend.BlogsBackend.Security.entity.*;
import com.Blogs.Backend.BlogsBackend.Security.exceptions.NoContentException;
import com.Blogs.Backend.BlogsBackend.Security.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    // Repositories are autowired for CRUD operations on seller requests and user information
    @Autowired
    private SellerRequestRepository sellerRequestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private RejectedSellerRequestRepository rejectedSellerRequestRepository;
    @Autowired
    private UserService userService;

    // Retrieves all pending seller requests, throws NoContentException if none are found
    public List<SellerRequest> getAllRequests() {
        List<SellerRequest> result = sellerRequestRepository.findAll();
        if(result.isEmpty()){
            throw new NoContentException(); // Throws exception if no seller requests are found
        }
        return result; // Returns list of all seller requests
    }

    // Approves a seller request based on the username provided in requestStringDto
    public boolean approveRequest(String userName) {
        // Finds the seller request by username
        SellerRequest sellerRequest = sellerRequestRepository.findByUserName(userName).orElse(null);
        System.out.println(userName); // Logs the username for debugging
        if(sellerRequest == null){
            throw new IllegalArgumentException("seller request not found"); // Throws exception if request does not exist
        }

        // Creates a new seller from the approved seller request details
        Seller seller = new Seller();
        seller.setUserName(sellerRequest.getUserName());

        // Saves the seller information to the seller repository
        sellerRepository.save(seller);

        // Saves the user object from the request into the user repository
        userRepository.save(sellerRequest.getUserObject());

        // Deletes the approved request from the seller request repository
        sellerRequestRepository.delete(sellerRequest);

        return true; // Indicates approval was successful
    }

    // Rejects a seller request based on the username provided in requestStringDto
    public boolean rejectRequest(String userName) {
        // Finds the seller request by username
        SellerRequest sellerRequest = sellerRequestRepository.findByUserName(userName).orElse(null);
        if(sellerRequest == null){
            throw new IllegalArgumentException("seller request not found"); // Throws exception if request does not exist
        }

        // Creates a new rejected request entity to store in the rejected request repository
        RejectSellerRequest rejectSellerRequest = new RejectSellerRequest();
        rejectSellerRequest.setUser(sellerRequest);

        // Saves the rejected request
        rejectedSellerRequestRepository.save(rejectSellerRequest);

        // Deletes the request from seller request repository by username
        sellerRequestRepository.deleteByUserName(userName);

        return true; // Indicates rejection was successful
    }

    // Retrieves all rejected seller requests, throws NoContentException if none are found
    public List<RejectSellerRequest> getAllRejectedRequests() {
        List<RejectSellerRequest> result = rejectedSellerRequestRepository.findAll();
        if(result.isEmpty()){
            throw new NoContentException(); // Throws exception if no rejected requests are found
        }
        return result; // Returns list of all rejected requests
    }

    // Retrieves a specific rejected request by ID, throws NoContentException if not found
    public RejectSellerRequest getRejectedRequest(String data) {
        RejectSellerRequest result = rejectedSellerRequestRepository.findById(data).orElse(null);
        if(result == null){
            throw new NoContentException("No request found"); // Throws exception if the rejected request is not found
        }
        return result; // Returns the specific rejected request
    }

    public List<User> getAllUsersAccount() {
        return userRepository.findAll();
    }

    public void deleteUser(String userName) {
        userService.deleteUser(userName);
    }
}
