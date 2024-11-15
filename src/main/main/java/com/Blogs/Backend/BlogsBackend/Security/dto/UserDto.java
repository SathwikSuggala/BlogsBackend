package com.Blogs.Backend.BlogsBackend.Security.dto;


import com.Blogs.Backend.BlogsBackend.Security.entity.User;
import com.Blogs.Backend.BlogsBackend.Security.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String userName;
    private String password;
    private String email;
    private String mobileNumber;

    // Contact Info
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    private Gender gender;  // Enum stored as string in MongoDB

    public void setUserDto(User user){
        setUserName(user.getUserName());
        setPassword(null);
        setEmail(user.getEmail());
        setMobileNumber(user.getMobileNumber());
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
        setDateOfBirth(user.getDateOfBirth());
        setGender(user.getGender());
    }
}
