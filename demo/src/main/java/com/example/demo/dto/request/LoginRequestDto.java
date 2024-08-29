package com.example.demo.dto.request;

import lombok.Data;

@Data
public class LoginRequestDto {
    String code;
    String provider;
}
