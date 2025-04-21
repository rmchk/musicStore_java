package com.example.musicStore.service;

import com.example.musicStore.model.ReCaptchaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class ReCaptchaService {

    @Value("${google.recaptcha.key.secret}")
    private String recaptchaSecret;

    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    public boolean verify(String recaptchaResponse) {
        RestTemplate restTemplate = new RestTemplate();
        String url = RECAPTCHA_VERIFY_URL + "?secret=" + recaptchaSecret + "&response=" + recaptchaResponse;

        try {
            ReCaptchaResponse response = restTemplate.getForObject(url, ReCaptchaResponse.class);
            return response != null && response.isSuccess();
        } catch (RestClientException e) {
            e.printStackTrace();
            return false;
        }
    }
}