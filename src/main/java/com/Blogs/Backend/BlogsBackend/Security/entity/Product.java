package com.Blogs.Backend.BlogsBackend.Security.entity;


import com.Blogs.Backend.BlogsBackend.Security.enums.ProductCategory;
import com.Blogs.Backend.BlogsBackend.Security.dto.ProductDto;
import jakarta.persistence.EntityListeners;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Product {
    
    @Id
    private String id;
    @Indexed(unique = true)
    private String productName;
    private int quantity;
    private ProductCategory category;
    private double price;
    private String description;
    private String imageUrl;

    private String createdBy; // ID reference to Admin or User
    //private String updatedBy; // ID reference to Admin or User
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void setProduct(ProductDto productDto, String createdBy){

        productName= productDto.getProductName();
        quantity = productDto.getQuantity();
        category = productDto.getCategory();
        price = productDto.getPrice();
        description = productDto.getDescription();
        this.createdBy = createdBy;
        imageUrl = productDto.getImageUrl();
    }

}
