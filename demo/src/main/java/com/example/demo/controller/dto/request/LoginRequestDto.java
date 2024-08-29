package com.example.demo.controller.dto.request;

import lombok.Data;

@Data
public class LoginRequestDto {
    String code;
    String provider;
}
