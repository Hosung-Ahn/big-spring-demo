package com.example.demo.oauth2.api;

import com.example.demo.oauth2.dto.OAuth2AccessTokenResponse;
import com.example.demo.oauth2.util.property.OAuth2Property;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

@Service
public class OAuth2Fetcher {
    public OAuth2AccessTokenResponse getAccessToken(String code, OAuth2Property oAuth2Property) {
        return WebClient.create()
                .post()
                .uri(oAuth2Property.getTokenUri())
                .headers(header -> {
                    header.setBasicAuth(oAuth2Property.getClientId(), oAuth2Property.getClientSecret());
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(accessTokenRequestForm(code, oAuth2Property))
                .retrieve()
                .bodyToMono(OAuth2AccessTokenResponse.class)
                .block();
    }

    private MultiValueMap<String, String> accessTokenRequestForm(String code, OAuth2Property oAuth2Property) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("code", code);
        form.add("grant_type", "authorization_code");
        form.add("redirect_uri", oAuth2Property.getRedirectUri());
        return form;
    }

    public Map<String, Object> getUserAttributes(String accessToken, OAuth2Property oAuth2Property) {
        return WebClient.create()
                .post()
                .uri(oAuth2Property.getUserInfoUri())
                .headers(header -> {
                    header.setBearerAuth(accessToken);
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }
}
