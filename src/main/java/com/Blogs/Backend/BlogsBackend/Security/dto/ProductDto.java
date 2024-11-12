package com.Blogs.Backend.BlogsBackend.Security.dto;

import com.Blogs.Backend.BlogsBackend.Security.enums.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    @Id
    private String id;
    private String productName;
    private int quantity;
    private ProductCategory category;
    private double price;
    private String description;
    private String imageUrl;
}
