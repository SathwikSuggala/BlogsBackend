package com.Blogs.Backend.BlogsBackend.Security.classes;

import com.Blogs.Backend.BlogsBackend.Security.dto.AddProductToCartRequestDto;
import com.Blogs.Backend.BlogsBackend.Security.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartProduct {

    private String productId;
    private String productName;
    private String price;
    private String quantity;

    public CartProduct(AddProductToCartRequestDto dto, Product product){

        this.productId = dto.getProductId();
        this.productName = product.getProductName();
        this.price = String.valueOf((int) product.getPrice());
        this.quantity = String.valueOf(dto.getQuantity());
    }

}
