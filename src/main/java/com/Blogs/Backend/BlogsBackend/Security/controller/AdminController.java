package com.Blogs.Backend.BlogsBackend.Security.controller;


import com.Blogs.Backend.BlogsBackend.Security.dto.RequestStringDto;
import com.Blogs.Backend.BlogsBackend.Security.exceptions.NoContentException;
import com.Blogs.Backend.BlogsBackend.Security.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin
public class AdminController {

    @Autowired
    private AdminService adminService;

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

    @GetMapping("/getAllOrders")
    public ResponseEntity<?> getAllOrders(){
        return new ResponseEntity<>(adminService.getAllOrders(),HttpStatus.OK);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers(){
        return new ResponseEntity<>(adminService.getAllUsersAccount(),HttpStatus.OK);
    }
}
