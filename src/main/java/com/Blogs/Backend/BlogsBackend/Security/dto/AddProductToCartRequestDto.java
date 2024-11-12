package com.Blogs.Backend.BlogsBackend.Security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddProductToCartRequestDto {

    private String productId;
    private int quantity;

}
