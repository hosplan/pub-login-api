package com.iuni.login.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SignIn {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String nickName;
    private String credential;
    private String accessToken;
    private String refreshToken;
    private String jwt;
    private Boolean certification;
    private String code;
    private List<Authority> authList;
    private Avatar avatar;
}
