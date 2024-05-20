package com.iuni.login.service;

import com.iuni.login.domain.*;
import com.iuni.login.helper.Auth;
import com.iuni.login.helper.Encrypt;
import com.iuni.login.helper.jwt.CustomUserDetails;
import com.iuni.login.helper.jwt.JWT;
import com.iuni.login.helper.oauth.kakao.KakaoUserInfo;
import com.iuni.login.helper.redis.RedisService;
import com.iuni.login.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service("memberService")
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final RedisService redisService;
    private final AmazonSMTPService amazonSMTPService;
    private final MemberIuniMapRepository memberIuniMapRepository;
    private final IuniStyleRepository iuniStyleRepository;
    private final AlarmRepository alarmRepository;
    private void initCreateIuniCat(Member member){
        IuniStyle data = new IuniStyle();
        data.setBackground("#b7bbff");
        data.setBody("#5762ff");
        data.setLeftWhiteEye("#fff");
        data.setLeftEye("#020918");
        data.setRightWhiteEye("#fff");
        data.setRightEye("#020918");
        data.setNose("#fff");
        IuniStyle iuniStyle = iuniStyleRepository.save(data);

        MemberIuniMap memberIuniMap = new MemberIuniMap();
        memberIuniMap.setIuniStyle(iuniStyle);
        memberIuniMap.setMember(member);
        memberIuniMap.setIsMain(true);
        memberIuniMapRepository.save(memberIuniMap);
    }
    public Boolean save(Member member){
        try{
            //회원 생성
            Encrypt encrypt = new Encrypt();
            member.setCertification(false);
            Member saveMember = this.memberRepository.save(encrypt.makeData(member));

            //회원 권한 생성
            Authority result = saveAuth("TEMPUSER", saveMember);

            initCreateIuniCat(saveMember);

            return result.getId() != null;
        }
        catch(Exception e){
            throw e;
        }
    }

    public Avatar makeAvatar(IuniStyle data){
        Avatar avatar = new Avatar();
        avatar.setBackground(data.getBackground());
        avatar.setHair(data.getHair());
        avatar.setBody(data.getBody());
        avatar.setNose(data.getNose());
        avatar.setLeftEye(data.getLeftEye());
        avatar.setLeftEar(data.getLeftEar());
        avatar.setType(data.getType());
        avatar.setLeftFace(data.getLeftFace());
        avatar.setRightEar(data.getRightEar());
        avatar.setRightFace(data.getRightFace());
        avatar.setLeftWhiteEye(data.getLeftWhiteEye());
        avatar.setRightWhiteEye(data.getRightWhiteEye());
        avatar.setNose(data.getNose());
        return avatar;
    }

    public SignIn findRequestMember(String email, Long relateId, String alarmType){
        Optional<Member> optMember = memberRepository.findByEmail(email);
        SignIn result = new SignIn();
        if(optMember.isEmpty()){
            return result;
        }
        Member member = optMember.get();
        //기존에 요청이 있는지 확인
        ArrayList<Alarm> dataList = alarmRepository.findByToMemberAndRelateIdAndAlarmType(member, relateId, alarmType).orElse(new ArrayList<>());
        if(!dataList.isEmpty()){
           result.setId(-1L);
           return result;
        }

        MemberIuniMap memberIuniMap = memberIuniMapRepository.findById(member.getMemberIuniMaps().get(0).getId()).orElse(new MemberIuniMap());
        result.setId(member.getId());
        result.setEmail(member.getEmail());
        result.setNickName(member.getNickName());
        result.setAvatar(makeAvatar(memberIuniMap.getIuniStyle()));

        return result;
    }
    public SignIn findEmail(String email){
        try{
            System.out.println("email = " + email);
            Optional<Member> optMember = memberRepository.findByEmail(email);
            SignIn result = new SignIn();
            if(optMember.isEmpty()){
                return result;
            }

            Member member = optMember.get();
            System.out.println("member.getMemberIuniMaps().get(0).getId() = " + member.getMemberIuniMaps().get(0).getId());
            MemberIuniMap memberIuniMap = memberIuniMapRepository.findById(member.getMemberIuniMaps().get(0).getId()).orElse(new MemberIuniMap());
            
            result.setId(member.getId());
            result.setEmail(member.getEmail());
            result.setNickName(member.getNickName());
            result.setAvatar(makeAvatar(memberIuniMap.getIuniStyle()));

            return result;
        }
        catch(Exception e){
            System.out.println("findEmailError = " + e);
            throw e;
        }
    }

    //비밀번호 변경 인증코드 보내기
    public String certificateCode(CertificateCode data){
        try{
            //시리얼 코드 생성
            String serialCode = redisService.getSerialCode();

            //인증코드 전송
            Map<String, Object> variables = Map.of("data", serialCode);
            amazonSMTPService.send("iUniverse 인증코드 입니다.", variables, data.getEmail());
            //레디스 등록 5분
            Duration duration = Duration.ofMinutes(5);
            redisService.setValues(serialCode, data.getEmail(), duration);

            return "success";
        }
        catch(Exception e){
            System.out.println("certificateCodeError : " + e);
            return "fail";
        }
    }

    //비밀번호 변경 인증코드 확인
    public String checkCertification(CertificateCode data){
        try{
            //시리얼 코드 확인
            String result = redisService.getValues(data.getCode());
            if(result.equals("false")){
                return result;
            }

            return "success";
        }
        catch(Exception e){
            System.out.println("checkCertificationError : " + e);
            return "false";
        }
    }

    public String changePw(Member data){
        try{
            //이메일로 실제 존재하는 이메일인지 확인
            Optional<Member> optMember = memberRepository.findByEmail(data.getEmail());

            if(optMember.isEmpty()){
                return "NotExistEmail";
            }

            //비밀번호 암호화 및 업데이트
            Encrypt encrypt = new Encrypt();
            Member tempMember = optMember.get();
            tempMember.setPassword(data.getPassword());
            Member updateMember = encrypt.makeData(tempMember);
            if(updateMember.getType() == null){
                Member saveMember = memberRepository.save(updateMember);
                return saveMember.getId() != null ? "success" : "false";
            }
            else {
                return updateMember.getType();
            }
        }
        catch(Exception e){
            System.out.println("changePwError : " + e);
            return "false";
        }
    }

    //권한 생성
    public Authority saveAuth(String type, Member member){
        try{
            Auth auth = new Auth();
            return this.authorityRepository.save(auth.makeData(type, member));
        }
        catch(Exception e){
            throw e;
        }
    }

    //구글 회원 가입
    public Member saveGoogleOAuth(SignIn data){
        try{
            Member member = new Member();
            member.setCertification(true);
            member.setEmail(data.getEmail());
            member.setName(data.getNickName());
            member.setNickName(data.getNickName());
            member.setType("google");
            member.setIsSocial(true);
            member.setCredential(data.getCredential());
            Member saveMember = memberRepository.save(member);

            initCreateIuniCat(saveMember);
            saveAuth("USER", saveMember);
            saveAuth("TEMPUSER", saveMember);
            return saveMember;
        }
        catch(Exception e){
            throw e;
        }
    }

    public Optional<Member> findById(Long id){
        try{
            return memberRepository.findById(id);
        }
        catch(Exception e){
            throw e;
        }
    }
    //카카오 회원 가입
    public Member saveKakaoOAuth(KakaoUserInfo data){
        try{
            Member member = new Member();
            member.setCertification(true);
            member.setEmail(data.getEmail());
            member.setType("kakao");
            member.setNickName(data.getNickname());
            member.setIsSocial(true);
            member.setName(data.getName());
            member.setCredential(data.getAccessCode());
            Member saveMember = memberRepository.save(member);
            initCreateIuniCat(saveMember);
            saveAuth("USER", saveMember);
            saveAuth("TEMPUSER", saveMember);
            return saveMember;
        }
        catch(Exception e){
            throw e;
        }
    }

    public Boolean checkCredential(String accessToken) {
        try{
            System.out.println(accessToken);
            Optional<Member> member = this.memberRepository.findByCredential(accessToken);
            System.out.println(member.isPresent());
            return member.isPresent();
        }
        catch(Exception e){
            throw e;
        }
    }


    public Member updateMemberRole(SignIn data){
        try{
            Optional<Member> optionalMember = memberRepository.findByEmail(data.getEmail());
            if(optionalMember.isEmpty()){
                return new Member();
            }
            
            Member result = optionalMember.get();
            //권한 생성

            List<Authority> authorityList = result.getRoles();
            Auth auth = new Auth();
            result.setCertification(true);
            memberRepository.save(result);
            authorityList.add( this.authorityRepository.save(auth.makeData("USER", result)));
            result.setRoles(authorityList);

            return result;
        }
        catch(Exception e){
            throw e;
        }
    }
    public Boolean updateBasicInfo(Member data){
        try{
            Optional<Member> optionalMember = memberRepository.findById(data.getId());

            if(optionalMember.isPresent()){
                Member member = optionalMember.get();
                member.setCertification(data.getCertification());
                member.setIsSocial(data.getIsSocial());
                member.setCredential(data.getCredential());
                member.setName(data.getName());
                member.setEmail(data.getEmail());
                member.setNickName(data.getNickName());
                member.setPassword(data.getPassword());

                memberRepository.save(member);

                return true;
            } else {
                return false;
            }
        }
        catch(Exception e){
            throw e;
        }
    }


    //이메일이 있는지 검사
    public Boolean checkEmail(String email){
        try{
            Optional<Member> member = this.memberRepository.findByEmail(email);
            return member.isPresent();
        }
        catch(Exception e){
            throw e;
        }
    }

    public Boolean checkPw(String pw, Long id){
        try{
            Optional<Member> optionalMember = memberRepository.findById(id);
            if(optionalMember.isEmpty()){
                return false;
            }
            Member member = optionalMember.get();
            Encrypt encrypt = new Encrypt();
            String hashPw = encrypt.getEncrypt(pw, member.getSalt());
            Optional<Member> pwCheck = memberRepository.findByEmailAndPassword(member.getEmail(), hashPw);
            return pwCheck.isPresent();
        }
        catch(Exception e){
            return false;
        }
    }

    public Boolean unscribe(Long memberId){
        try{
            Optional<Member> optMember = memberRepository.findById(memberId);
            if(optMember.isEmpty()){
                return false;
            }
            Member member = optMember.get();

            memberIuniMapRepository.deleteAllByMember(memberId);
            alarmRepository.deleteAllByToMemberOrFromMember(memberId);
            authorityRepository.deleteAllByMember(memberId);
            memberRepository.delete(member);
            return true;
        }
        catch(Exception e){
            System.out.println("e = " + e);
            return false;
        }
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        Member member = this.memberRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("잘못된 인증 입니다.")
        );
        return new CustomUserDetails(member);
    }
}