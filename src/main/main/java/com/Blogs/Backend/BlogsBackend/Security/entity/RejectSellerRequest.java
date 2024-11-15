package com.Blogs.Backend.BlogsBackend.Security.entity;

import com.Blogs.Backend.BlogsBackend.Security.enums.Gender;
import com.Blogs.Backend.BlogsBackend.Security.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RejectSellerRequest {

    @Id  // Primary key in MongoDB
    private String id;

    private String userName;
    private String password;
    private String email;
    private String mobileNumber;

    private Role role;  // Enum stored as string in MongoDB

    // Contact Info
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    private Gender gender;  // Enum stored as string in MongoDB


    // Metadata
    @CreatedDate  // Automatically sets the creation date
    private LocalDateTime createdAt;

    @LastModifiedDate  // Automatically updates the modification date
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public void setUser(SellerRequest dto){
        setUserName(dto.getUserName());
        setPassword(dto.getPassword());
        setEmail(dto.getEmail());
        setMobileNumber(dto.getMobileNumber());
        setRole(Role.SELLER);
        setFirstName(dto.getFirstName());
        setLastName(dto.getLastName());
        setDateOfBirth(dto.getDateOfBirth());
        setGender(dto.getGender());
        //setDeletedAt(null);
    }

    @BsonIgnore
    @JsonIgnore
    public User getUserObject() {

        User user = new User();
        user.setUserName(this.getUserName());
        user.setPassword(this.getPassword());
        user.setEmail(this.getEmail());
        user.setMobileNumber(this.getMobileNumber());
        user.setRole(Role.SELLER);  // Setting role as SELLER by default
        user.setFirstName(this.getFirstName());
        user.setLastName(this.getLastName());
        user.setDateOfBirth(this.getDateOfBirth());
        user.setGender(this.getGender());

        return user;
    }
}
