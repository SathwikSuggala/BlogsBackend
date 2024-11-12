package com.Blogs.Backend.BlogsBackend.Security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto{

    private String id;
    private String addressName;
    private String fullName;
    private String mobileNumber;
    private String houseNumber;
    private String street;
    private String landMark;
    private int pinCode;
    private String city;
    private String state;
    private String country;

}