package com.example.Stock.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
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
    private LocalDateTime tokenExpiry = null;

    public String getAccessToken() {
        // 토큰이 있고 만료 안됐으면 재사용
        if (cachedToken != null && tokenExpiry != null && LocalDateTime.now().isBefore(tokenExpiry)) {
            return cachedToken;
        }

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
        tokenExpiry = LocalDateTime.now().plusHours(23); // 24시간 유효
        return cachedToken;
    }
}