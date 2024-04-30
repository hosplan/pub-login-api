package com.iuni.login.service;

import com.iuni.login.domain.IuniToken;
import com.iuni.login.domain.Member;
import com.iuni.login.domain.SignIn;
import com.iuni.login.helper.Encrypt;
import com.iuni.login.helper.jwt.JWT;
import com.iuni.login.helper.oauth.kakao.KakaoUserInfo;
import com.iuni.login.repository.MemberRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service("signInService")
public class SignInService {
    private final MemberRepository memberRepository;
    private final JWT jwt;

    public SignInService(MemberRepository memberRepository,
                         JWT jwt){
        this.memberRepository = memberRepository;
        this.jwt = jwt;
    }
    public IuniToken updateJWT(Member member){
        return jwt.generate(member.getEmail(), member.getRoles(), member.getId(), member.getNickName());
    }

    public SignIn getBasicInfo(String token){

        Optional<Member> optMember = memberRepository.findById(jwt.getId(token));
        Member member = optMember.get();

        SignIn result = new SignIn();
        result.setNickName(member.getNickName());
        result.setName(member.getName());
        result.setEmail(member.getEmail());
        result.setCertification(member.getCertification());
        result.setId(member.getId());

        return result;
    }

    //로그인
    public IuniToken signIn(SignIn data){
        //이메일 체크
        Optional<Member> emailCheck = memberRepository.findByEmail(data.getEmail());
        if(emailCheck.isEmpty()){
            IuniToken result = new IuniToken();
            result.setResult("NotMatchEmail");
            return result;
        }
        //비밀번호 체크
        Encrypt encrypt = new Encrypt();
        String hashPw = encrypt.getEncrypt(data.getPassword(), emailCheck.get().getSalt());
        Optional<Member> pwCheck = memberRepository.findByEmailAndPassword(emailCheck.get().getEmail(), hashPw);
        if(pwCheck.isEmpty()){
            IuniToken result = new IuniToken();
            result.setResult("NotMatchPassword");
            return result;
        }
        //토큰 생성
        return jwt.generate(pwCheck.get().getEmail(), pwCheck.get().getRoles(), pwCheck.get().getId(), pwCheck.get().getNickName());
    }

    //카카오 로그인
    public IuniToken signInKaKaoOAuth(KakaoUserInfo kakaoUserInfo){
        Optional<Member> optOauthMember = memberRepository.findByEmailAndType(kakaoUserInfo.getEmail(), "kakao");
        if(optOauthMember.isEmpty()){
            IuniToken result = new IuniToken();
            result.setResult("NotCollectOAuth");
            return result;
        }
        return jwt.generate(optOauthMember.get().getEmail(), optOauthMember.get().getRoles(), optOauthMember.get().getId(), optOauthMember.get().getNickName());
    }

    //구글 로그인
    public IuniToken signInGoogleOAuth(SignIn userInfo){
        Optional<Member> optOauthMember = memberRepository.findByEmailAndType(userInfo.getEmail(), "google");
        if(optOauthMember.isEmpty()){
            IuniToken result = new IuniToken();
            result.setResult("NotCollectOAuth");
            return result;
        }

        return jwt.generate(optOauthMember.get().getEmail(), optOauthMember.get().getRoles(), optOauthMember.get().getId(), optOauthMember.get().getNickName());
    }
}
