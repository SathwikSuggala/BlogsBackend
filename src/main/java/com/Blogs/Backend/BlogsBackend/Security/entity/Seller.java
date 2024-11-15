package com.Blogs.Backend.BlogsBackend.Security.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collection = "seller")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seller {

    @Id
    private String id;
    private String userName;
    private List<String> BlogId = new ArrayList<>();

}
