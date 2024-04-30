package com.iuni.login.helper.oauth.kakao;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ReqToken {

    private final String grant_type = "authorization_code";
    private final String client_id = "1b41f9bb62fc1386b35e7989f21c4520";
    private final String redirect_url = "https://iuniverse.me";
    @Setter
    private String code;
}
