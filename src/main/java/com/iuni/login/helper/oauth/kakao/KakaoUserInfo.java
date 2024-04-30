package com.iuni.login.helper.oauth.kakao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoUserInfo {
    private String nickname;
    private String name;
    private String email;
    private String accessCode;
}
