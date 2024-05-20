package com.iuni.login.controller;

import com.iuni.login.domain.*;
import com.iuni.login.helper.jwt.JWT;
import com.iuni.login.helper.redis.RedisService;
import com.iuni.login.service.AmazonSMTPService;
import com.iuni.login.service.MemberService;
import com.iuni.login.service.SignInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/member")
public class MemberController {
    private final MemberService memberService;
    private final SignInService signInService;
    private final RedisService redisService;
    private final AmazonSMTPService amazonSMTPService;
    private final JWT jwt;

    @GetMapping()
    public ResponseEntity<Boolean> checkEmail(@RequestParam("email") String email){
        return new ResponseEntity<>(memberService.checkEmail(email), HttpStatus.OK);
    }

    @PostMapping("/checkPw")
    public ResponseEntity<Boolean> checkPw(@RequestBody SignIn signIn, @RequestHeader Map<String, String> headers){
        return new ResponseEntity<>( memberService.checkPw(signIn.getPassword(), jwt.getId(headers.get("authorization"))), HttpStatus.OK );
    }


    @PostMapping()
    public HashMap<String, Boolean> signUp(@RequestBody Member data){
        HashMap<String, Boolean> result = new HashMap<>();
        result.put("result", memberService.save(data));
        return result;
    }

    @PostMapping("/certificatecode")
    public HashMap<String, String> certificateCode(@RequestBody CertificateCode data){
        HashMap<String, String> result = new HashMap<>();
        result.put("result", memberService.certificateCode(data));
        return result;
    }

    @PostMapping("/checkcertification")
    public HashMap<String, String> checkCertification(@RequestBody CertificateCode data){
        HashMap<String, String> result = new HashMap<>();
        result.put("result", memberService.checkCertification(data));
        return result;
    }

    @PostMapping("/changepw")
    public HashMap<String, String> changePw(@RequestBody Member data){
        HashMap<String, String> result = new HashMap<>();
        result.put("result", memberService.changePw(data));
        return result;
    }

    @GetMapping("/oauth")
    public Boolean checkCredential(@RequestParam("accessToken") String accessToken){
        return memberService.checkCredential(accessToken);
    }


    //사용자 정보 가져오기
    @PostMapping("/my")
    public SignIn getMyNickName(@RequestHeader Map<String, String> headers){
        return signInService.getBasicInfo(headers.get("authorization"));
    }

    //이메일이 전송 되었을때 코드 Redis에 등록
    @PostMapping("/certification")
    public HashMap<String, String> sendSerialCode(@RequestBody Member data){
        HashMap<String, String> result = new HashMap<>();
        try{
            String serialCode = this.redisService.getSerialCode();
            Map<String, Object> variables = Map.of("data", serialCode);
            this.amazonSMTPService.send("iUniverse 인증코드 입니다", variables, data.getEmail());

            //redis
            Duration duration = Duration.ofMinutes(5);
            this.redisService.setValues(data.getEmail(), serialCode, duration);

            result.put("result", this.redisService.getValues(data.getEmail()));
            return result;
        }
        catch(Exception e) {
            System.out.println("error = " + e);
            result.put("result", "fail");
            return result;
        }
    }

    //이메일 찾기
    @GetMapping("/share")
    public SignIn findEmail(@RequestParam("email") String email, @RequestParam("relateId") Long relatedId, @RequestParam("alarmType") String alarmType){
        return memberService.findRequestMember(email, relatedId, alarmType);
    }


    //전송 코드 확인후 인증 완료 변경 및 Redis 값 삭제
    @PostMapping("/checkCode")
    public HashMap<String, IuniToken> checkCode(@RequestBody SignIn data){
        HashMap<String, IuniToken> result = new HashMap<>();
        String certificationCode = redisService.getValues(data.getEmail());

        if(!certificationCode.equals(data.getCode())){
            IuniToken iuniToken = new IuniToken();
            iuniToken.setResult("NoCollectCode");
            result.put("result", iuniToken);
            return result;
        }

        redisService.deleteValues(data.getEmail());
        result.put("result", signInService.updateJWT(memberService.updateMemberRole(data)));
        return result;
    }
    //탈퇴
    @PatchMapping("/unscribe")
    public ResponseEntity<Boolean> unscribe( @RequestHeader Map<String, String> headers){
        return new ResponseEntity<>( memberService.unscribe(jwt.getId(headers.get("authorization"))), HttpStatus.OK );

    }
    @PatchMapping()
    public Boolean update(@RequestBody Member member){
        return this.memberService.updateBasicInfo(member);
    }
}
