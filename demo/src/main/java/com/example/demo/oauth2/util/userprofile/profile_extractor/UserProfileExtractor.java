package com.example.demo.oauth2.util.userprofile.profile_extractor;

import com.example.demo.oauth2.util.userprofile.data.UserProfileData;

import java.util.Map;

public interface UserProfileExtractor {
    UserProfileData extract(Map<String, Object> attributes);
}
