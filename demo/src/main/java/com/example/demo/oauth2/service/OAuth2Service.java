package com.example.demo.oauth2.service;

import com.example.demo.oauth2.OAuth2Provider;
import com.example.demo.oauth2.api.OAuth2Fetcher;
import com.example.demo.oauth2.dto.OAuth2AccessTokenResponse;
import com.example.demo.oauth2.util.property.OAuth2Properties;
import com.example.demo.oauth2.util.property.OAuth2Property;
import com.example.demo.oauth2.util.userprofile.UserProfileExtractorFactory;
import com.example.demo.oauth2.util.userprofile.dto.UserProfileDto;
import com.example.demo.oauth2.util.userprofile.profile_extractor.UserProfileExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2Service {
    private final OAuth2Fetcher oAuth2Fetcher;
    private final OAuth2Properties oAuth2Properties;
    private final UserProfileExtractorFactory userProfileExtractorFactory;

    public UserProfileDto getUserProfile(String code, OAuth2Provider oAuth2Provider) {
        OAuth2Property oAuth2Property = oAuth2Properties.get(oAuth2Provider);

        OAuth2AccessTokenResponse accessTokenResponse = oAuth2Fetcher.getAccessToken(code, oAuth2Property);

        Map<String, Object> attributes = oAuth2Fetcher.getUserAttributes(accessTokenResponse.getAccessToken(), oAuth2Property);

        UserProfileExtractor userProfileExtractor = userProfileExtractorFactory.get(oAuth2Provider);
        return userProfileExtractor.extract(attributes);
    }
}
