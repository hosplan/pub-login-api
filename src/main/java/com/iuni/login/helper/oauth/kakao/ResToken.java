package com.iuni.login.helper.oauth.kakao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResToken {
    private String token_type;

    private String access_token;

    private String id_token;

    private int expires_in;

    private String refresh_token;

    private int refresh_token_expires_in;
}
