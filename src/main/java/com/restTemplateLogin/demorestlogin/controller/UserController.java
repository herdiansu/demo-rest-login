package com.restTemplateLogin.demorestlogin.controller;

import com.restTemplateLogin.demorestlogin.model.ResponseDto;
import com.restTemplateLogin.demorestlogin.service.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

@RestController
public class UserController {

    public static final String REST_SEND_MSG = "http://localhost:8095/soafwd/apipub/send";
    static RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private RestClient restClient;

    @RequestMapping(value = "/hello")
    public ResponseEntity<String> testpost(HttpServletRequest req) {
        String addr = "Hello";
        return ResponseEntity.ok(addr);
    }


    @RequestMapping(value = "/getToken", method = RequestMethod.GET)
    public String getLogin(){
        return restClient.getLogin();
    }

   @PostMapping("/send")
   public ResponseEntity<ResponseDto> send(@RequestBody String message) throws NoSuchAlgorithmException {


       MessageDigest md = MessageDigest.getInstance("MD5");
       md.update(message.getBytes());
       String md5 = DatatypeConverter.printHexBinary(md.digest());

       System.out.println("Sending Message");

       String authToken = "http://localhost:8090/getToken";

       RestTemplate restTemplate = new RestTemplateBuilder(rt-> rt.getInterceptors().add((request, body, execution) -> {
           request.getHeaders().add("Authorization", "Bearer "+ authToken);
           return execution.execute(request, body);
       })).build();

       HttpHeaders headers = new HttpHeaders();
//       headers.add("Authorization", "Bearer " + authToken);
       headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
       headers.setContentType(MediaType.TEXT_PLAIN);

       HttpEntity<String> entity = new HttpEntity<>("token", headers);
       restTemplate.postForEntity(REST_SEND_MSG, entity, ResponseDto.class);
       try{
           return new ResponseEntity<ResponseDto>(new ResponseDto("SUCCESS", md5), HttpStatus.OK);
       }catch (HttpClientErrorException e){
           return new ResponseEntity<ResponseDto>(new ResponseDto("FORBIDDEN", md5), HttpStatus.FORBIDDEN);
       }

   }
}
