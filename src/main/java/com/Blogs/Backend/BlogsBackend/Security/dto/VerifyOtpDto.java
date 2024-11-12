package com.Blogs.Backend.BlogsBackend.Security.dto;

import lombok.Data;

@Data
public class VerifyOtpDto {

    private String userName;
    private String smsOtp;
    private String emailOtp;

}
