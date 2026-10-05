package org.example.productservice.commons;

import org.example.productservice.dto.UserResponseDTOs;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthCommons {
    // To make a call to different REST Api call - Using RestTemplate
    private static RestTemplate restTemplate;

    public AuthCommons(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public static boolean validateToken(String token){
        try{
            // Call the UserService to validate token
            UserResponseDTOs userResponseDTOs =  restTemplate.getForObject("http://localhost:8082/users/validate/" + token, UserResponseDTOs.class);

            return userResponseDTOs != null;
        }catch(HttpClientErrorException exception){
            return false;
        }
    }
}
