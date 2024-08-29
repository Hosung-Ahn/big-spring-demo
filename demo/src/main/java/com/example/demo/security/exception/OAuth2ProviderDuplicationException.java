package com.example.demo.security.exception;

public class OAuth2ProviderDuplicationException extends RuntimeException{
    public OAuth2ProviderDuplicationException(String provider){
        super("OAuth2 provider " + provider + " is already registered.");
    }
}
