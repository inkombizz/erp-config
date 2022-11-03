package com.inkombizz.utils;



import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.inkombizz.AuthenticationConstant;
import com.inkombizz.entity.model.User;
import com.inkombizz.exeption.CustomException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestUtil {

  @Value(AuthenticationConstant.TOKEN_VALIDATOR_URL_KEY)
  private String url;

  public RestUtil() {
  }

  public User whoAmI(String token, String prefix) {
    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.set(AuthenticationConstant.AUTHORIZATION, token);

    Map<String, String> map = new HashMap<>();
    map.put(AuthenticationConstant.TOKEN, token);
    HttpEntity<Map<String, String>> entity = new HttpEntity<>(map, headers);
    ResponseEntity<User> response = restTemplate.postForEntity(url + prefix, entity, User.class);

    if (response.getStatusCode() == HttpStatus.OK) {
      return (User) response.getBody();
    }
    
    if (response.getStatusCode() == HttpStatus.NON_AUTHORITATIVE_INFORMATION) {
      throw new CustomException("Session Expired", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }
    return null;
  }
}
