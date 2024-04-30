package com.iuni.login.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IuniToken {
    private String accessToken;
    private String refreshToken;
    private String result;
}
