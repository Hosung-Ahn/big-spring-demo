package com.example.demo.oauth2.util.userprofile.profile_extractor;

import com.example.demo.oauth2.util.userprofile.dto.UserProfileDto;

import java.util.Map;

public interface UserProfileExtractor {
    UserProfileDto extract(Map<String, Object> attributes);
}
