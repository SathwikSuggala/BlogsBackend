package com.Blogs.Backend.BlogsBackend.Security.dto;

import com.Blogs.Backend.BlogsBackend.Security.classes.CartProduct;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartInfoDto {

    @Id
    private String Id;
    private List<CartProduct> products = new ArrayList<>();
    private String totalPrice;
}
