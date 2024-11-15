package com.Blogs.Backend.BlogsBackend.Security.entity;


import com.Blogs.Backend.BlogsBackend.Security.enums.Gender;
import com.Blogs.Backend.BlogsBackend.Security.enums.Role;
import com.Blogs.Backend.BlogsBackend.Security.dto.UserDto;
import jakarta.persistence.EntityListeners;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "users")  // This marks the class as a MongoDB document, with a collection named "users"
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {

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

     public void setUser(UserDto dto){
         setUserName(dto.getUserName());
         setPassword(dto.getPassword());
         setEmail(dto.getEmail());
         setMobileNumber(dto.getMobileNumber());
         setRole(Role.CUSTOMER);
         setFirstName(dto.getFirstName());
         setLastName(dto.getLastName());
         setDateOfBirth(dto.getDateOfBirth());
         setGender(dto.getGender());
         //setDeletedAt(null);
     }

    public void updateUser(User user) {
        // Assuming user is the incoming user object with potentially updated values

        if (userName == null) {
            userName = user.getUserName();
        }
        if (password == null) {
            password = user.getPassword();
        }
        if (email == null) {
            email = user.getEmail();
        }
        if (mobileNumber == null) {
            mobileNumber = user.getMobileNumber();
        }
        if (role == null) {
            role = user.getRole();
        }
        if (firstName == null) {
            firstName = user.getFirstName();
        }
        if (lastName == null) {
            lastName = user.getLastName();
        }
        if (dateOfBirth == null) {
            dateOfBirth = user.getDateOfBirth();
        }
        if (gender == null) {
            gender = user.getGender();
        }
    }

}
