package com.Blogs.Backend.BlogsBackend.Security.entity;


import com.Blogs.Backend.BlogsBackend.Security.classes.Address;
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
public class Orders {

    @Id
    private String Id;
    private String userName;
    private Address address;
    private List<CartProduct> cartProducts = new ArrayList<>();
    private String totalAmount;

}
