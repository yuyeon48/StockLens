package com.example.Stock.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KisTokenService {

    private final RestTemplate restTemplate;

    @Value("${kis.api.appkey}")
    private String appKey;

    @Value("${kis.api.appsecret}")
    private String appSecret;

    @Value("${kis.api.base-url}")
    private String baseUrl;

    private String cachedToken = null;

    public String getAccessToken() {
        if (cachedToken != null) return cachedToken;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("grant_type", "client_credentials");
        body.put("appkey", appKey);
        body.put("appsecret", appSecret);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl + "/oauth2/tokenP", request, Map.class
        );

        cachedToken = (String) response.getBody().get("access_token");
        return cachedToken;
    }
}