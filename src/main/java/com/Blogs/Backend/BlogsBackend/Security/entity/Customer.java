package com.Blogs.Backend.BlogsBackend.Security.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "customer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    private String id;
    private String userName;
    
}

