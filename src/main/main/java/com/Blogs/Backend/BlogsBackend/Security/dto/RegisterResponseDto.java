package com.Blogs.Backend.BlogsBackend.Security.dto;


import com.Blogs.Backend.BlogsBackend.Security.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponseDto {
    private String userName;
    private Role role;
    private boolean status;
}
