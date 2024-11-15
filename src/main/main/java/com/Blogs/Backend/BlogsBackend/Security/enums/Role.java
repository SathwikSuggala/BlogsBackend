package com.Blogs.Backend.BlogsBackend.Security.enums;


public enum Role {
    CUSTOMER,
    SELLER,
    ADMIN;

    @Override
    public String toString() {
        return "ROLE_" + name();
    }
}
