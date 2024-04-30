package com.iuni.login.helper.oauth.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;


public class KakaoOauth {
    public String getAccessToken(String code){
        try{

            HttpHeaders headers = new HttpHeaders();
            String url = "https://kauth.kakao.com/oauth/token";
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "authorization_code");
            body.add("client_id", "1b41f9bb62fc1386b35e7989f21c4520");
            body.add("redirect_uri", "http://localhost:3000");
            body.add("code", code);

            HttpEntity<MultiValueMap<String, String>> reqEntity = new HttpEntity<>(body, headers);
            JsonNode jsonNode = reqPost(reqEntity, url);
            return jsonNode.get("access_token").asText();
        }
        catch(Exception e){
            throw e;
        }
    }
    private JsonNode reqPost(HttpEntity<MultiValueMap<String, String>>  reqEntity, String url){
        try{
            RestTemplate rt = new RestTemplate();
            ResponseEntity<String> res = rt.exchange(
                    url,
                    HttpMethod.POST,
                    reqEntity,
                    String.class
            );

            String resBody = res.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(resBody);
        }
        catch(JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }

    public KakaoUserInfo getUserInfo(String accessToken){
        try{

            String url = "https://kapi.kakao.com/v2/user/me";
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            HttpEntity<MultiValueMap<String, String>> reqEntity = new HttpEntity<>(headers);
            JsonNode jsonNode = reqPost(reqEntity, url);

            KakaoUserInfo result = new KakaoUserInfo();

            result.setNickname(jsonNode.get("kakao_account").get("profile").get("nickname").asText());
            result.setName(jsonNode.get("kakao_account").get("profile").get("nickname").asText());
            result.setEmail(jsonNode.get("kakao_account").get("email").asText());
            result.setAccessCode(accessToken);
            return result;
        }
        catch(Exception e){
            throw e;
        }
    }
}
