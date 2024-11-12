package com.Blogs.Backend.BlogsBackend.Security.entity;

import com.Blogs.Backend.BlogsBackend.Security.classes.CartProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "cart")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    @Id
    private String id;
    private String userName;
    private List<CartProduct> products = new ArrayList<>();
}
