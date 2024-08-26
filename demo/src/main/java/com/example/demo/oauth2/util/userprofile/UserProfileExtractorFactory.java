package com.example.demo.oauth2.util.userprofile;

import com.example.demo.oauth2.OAuth2Provider;
import com.example.demo.oauth2.util.userprofile.profile_extractor.GithubUserProfileExtractor;
import com.example.demo.oauth2.util.userprofile.profile_extractor.GoogleUserProfileExtractor;
import com.example.demo.oauth2.util.userprofile.profile_extractor.NaverUserProfileExtractor;
import com.example.demo.oauth2.util.userprofile.profile_extractor.UserProfileExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserProfileExtractorFactory {
    private final GithubUserProfileExtractor gitHubUserProfileExtractor;
    private final GoogleUserProfileExtractor googleUserProfileExtractor;
    private final NaverUserProfileExtractor naverUserProfileExtractor;

    public UserProfileExtractor get(OAuth2Provider oAuth2Provider) {
        switch (oAuth2Provider) {
            case GOOGLE:
                return googleUserProfileExtractor;
            case NAVER:
                return naverUserProfileExtractor;
            case GITHUB:
                return gitHubUserProfileExtractor;
            default:
                throw new IllegalArgumentException("Invalid OAuth2 Provider");
        }
    }
}
