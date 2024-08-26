package com.example.demo.oauth2.util.userprofile.dto;

import lombok.Data;

@Data
public class UserProfileDto {
    private String name;
    private String email;
    private String imageUrl;

    public UserProfileDto(String name, String email, String imageUrl) {
        this.name = name;

        if (email == null || email.isEmpty() || email.isBlank()) {
            throw new IllegalArgumentException("there is no email in profile");
        } else {
            this.email = email;
        }

        this.imageUrl = imageUrl;
    }
}
