package com.restTemplateLogin.demorestlogin;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Map;

public class RestClient {

    public static final String REST_LOGIN_URL = "http://172.28.142.4:8095/soafwd/apipub/login";
    public static final String REST_SEND_MSG = "http://172.28.142.4:8095/soafwd/apipub/send";

    static RestTemplate restTemplate = new RestTemplate();
    public static void main(String[] args) {
         login();
    }

    private static HttpHeaders getHeaders(){
        String userCredentials = "ksei:ksei";
        String encodedCredentials =
                new String(Base64.encodeBase64(userCredentials.getBytes()));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Basic " + encodedCredentials);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return httpHeaders;
    }

    private static void login(){
        System.out.println("Getting Token");

        HttpHeaders httpHeaders = getHeaders();
        HttpEntity httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<TokenPojo> resp = restTemplate.exchange(REST_LOGIN_URL,
                HttpMethod.POST, httpEntity, TokenPojo.class);
        if (resp.getStatusCode() == HttpStatus.OK) {
            TokenPojo tokenPojo = resp.getBody();
            System.out.println("username = " + tokenPojo.getUsername() + "\ntoken = " + tokenPojo.getToken());
        }
    }

//    private static void sendMessage(){
//        System.out.println("Sending Message");
//
//        HttpHeaders httpHeaders = getHeaders();
//        HttpEntity httpEntity = new HttpEntity<>(httpHeaders);
//
//        ResponseEntity<String> resp = restTemplate.exchange(REST_SERVICE_URL,
//                HttpMethod.POST, httpEntity, String.class);
//        if (resp.getStatusCode() == HttpStatus.OK) {
//            String string = resp.getBody();
//            System.out.println(string);
//        }
//    }
}
