package com.iuni.login.controller;

import com.iuni.login.domain.IuniToken;
import com.iuni.login.domain.SignIn;
import com.iuni.login.helper.oauth.kakao.KakaoOauth;
import com.iuni.login.helper.oauth.kakao.KakaoUserInfo;
import com.iuni.login.service.MemberService;
import com.iuni.login.service.SignInService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/signIn")
public class SignInController {
    private final SignInService signInService;
    private final MemberService memberService;

    @PostMapping()
    public IuniToken signIn(@RequestBody SignIn data){
        return signInService.signIn(data);
    }

    @PostMapping("/kakao")
    public IuniToken kakaoSignIn(@RequestBody SignIn data){
        KakaoOauth kakaoOauth = new KakaoOauth();
        KakaoUserInfo kakaoUserInfo = kakaoOauth.getUserInfo(kakaoOauth.getAccessToken(data.getCredential()));
        if(!memberService.checkEmail(kakaoUserInfo.getEmail())){
            memberService.saveKakaoOAuth(kakaoUserInfo);
        }
        return signInService.signInKaKaoOAuth(kakaoUserInfo);
    }

    @PostMapping("/google")
    public IuniToken googleSignIn(@RequestBody SignIn data){
        if(!memberService.checkEmail(data.getEmail())){
            memberService.saveGoogleOAuth(data);
        }

        return signInService.signInGoogleOAuth(data);
    }
}
