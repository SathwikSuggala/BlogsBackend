package com.Blogs.Backend.BlogsBackend.Security.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class BlogRequest {

    @Id
    private String id;
    private String title;
    private String description;
    private String createdBy;
    private String imageUrl;
}
