package com.example.demo.oauth2.util.userprofile.profile_extractor;

import com.example.demo.oauth2.util.userprofile.data.UserProfileData;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GoogleUserProfileExtractor implements UserProfileExtractor{
    @Override
    public UserProfileData extract(Map<String, Object> attributes) {
        String name = (String) attributes.get("name");
        String email = (String) attributes.get("email");
        String imageUrl = (String) attributes.get("picture");

        return new UserProfileData(name, email, imageUrl);
    }
}
