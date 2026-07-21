package com.gearup.gearupbackend.model;


public class JwtAuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";

    private Long id;

    // Standard constructors
    public JwtAuthResponse() {
    }

    public JwtAuthResponse(String accessToken,  Long id) {
        this.accessToken = accessToken;
        this.id = id;
    }

    // Getters and Setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}