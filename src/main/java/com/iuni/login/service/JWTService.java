package com.iuni.login.service;

import com.iuni.login.domain.IuniToken;
import com.iuni.login.domain.Member;
import com.iuni.login.domain.SignIn;
import com.iuni.login.helper.jwt.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service("JWTService")
public class JWTService {
    private final JWT jwt;
    private final MemberService memberService;

    @Autowired
    public JWTService(JWT jwt, MemberService memberService){
        this.jwt = jwt;
        this.memberService = memberService;
    }

    public IuniToken reissuance(IuniToken data){
        IuniToken result = new IuniToken();
        //1. refresh token 값 가져오기
        String tokenInfo = jwt.getIdByRefreshToken(data);

        //2. 가져온 토큰 값의 id로 유저 정보 조회 expired 일경우 refresh 토큰도 만료된것
        if(Objects.equals(tokenInfo, "expired")){
            result.setResult(tokenInfo);
            return result;
        }

        Optional<Member> opt_member = memberService.findById(Long.parseLong(tokenInfo));
        if(opt_member.isEmpty()){
            result.setResult("noUser");
            return result;
        }
        //3.다시 access token, refreshToken 생성
        Member member = opt_member.get();
        return jwt.generate(member.getEmail(), member.getRoles(), member.getId(), member.getNickName());
    }
}
