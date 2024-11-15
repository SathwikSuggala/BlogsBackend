package com.Blogs.Backend.BlogsBackend.Security.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Blog {

    @Id
    private String id;
    private String title;
    private String description;
    private String createdBy;
    private String imageUrl;

    @JsonIgnore
    public  void setBlog(BlogRequest request){

        this.title = request.getTitle();
        this.description = request.getDescription();
        this.createdBy = request.getCreatedBy();
        this.imageUrl = request.getImageUrl();
    }
}
