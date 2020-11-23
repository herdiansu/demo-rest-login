package com.restTemplateLogin.demorestlogin.service;

import com.restTemplateLogin.demorestlogin.model.ResponseDto;
import com.restTemplateLogin.demorestlogin.model.TokenPojo;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;

@Service
public class RestClient {

    public static final String REST_LOGIN_URL = "http://localhost:8095/soafwd/apipub/login";
    static RestTemplate restTemplate = new RestTemplate();

    public static HttpHeaders getHeaders(){
        String userCredentials = "ksei:ksei";
        String encodedCredentials =
                new String(Base64.encodeBase64(userCredentials.getBytes()));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Basic " + encodedCredentials);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return httpHeaders;
    }

    public String getLogin() {
        System.out.println("Getting Token");

        HttpHeaders httpHeaders = getHeaders();
        HttpEntity httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<TokenPojo> resp = restTemplate.exchange(REST_LOGIN_URL,
                HttpMethod.POST, httpEntity, TokenPojo.class);
        TokenPojo tokenPojo = resp.getBody();
        String token = tokenPojo.getToken();
        return token;
    }

}
