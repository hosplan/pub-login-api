package com.iuni.login.service;

import com.iuni.login.domain.IuniStyle;
import com.iuni.login.domain.Member;
import com.iuni.login.domain.MemberIuniMap;
import com.iuni.login.helper.join.JoinIuniStyle;
import com.iuni.login.helper.jwt.JWT;
import com.iuni.login.repository.IuniStyleRepository;
import com.iuni.login.repository.MemberIuniMapRepository;
import com.iuni.login.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service("iuniStyleService")
@RequiredArgsConstructor
public class IuniStyleService {
    private final IuniStyleRepository iuniStyleRepository;
    private final MemberRepository memberRepository;
    private final MemberIuniMapRepository memberIuniMapRepository;
    private final JWT jwt;

    public IuniStyle create(IuniStyle data) { return this.iuniStyleRepository.save(data); }

    public IuniStyle init(){
        IuniStyle data = new IuniStyle();
        data.setBackground("#b7bbff");
        data.setBody("#5762ff");
        data.setLeftWhiteEye("#fff");
        data.setLeftEye("#020918");
        data.setRightWhiteEye("#fff");
        data.setRightEye("#020918");
        data.setNose("#fff");
        return this.iuniStyleRepository.save(data);
    }
    public ArrayList<IuniStyle> loadJoinMember(ArrayList<MemberIuniMap> dataList){
        try{
            ArrayList<IuniStyle> result = new ArrayList<>();
            for(MemberIuniMap map : dataList){
                System.out.println("map = " + map.getMemberId());
                JoinIuniStyle temp = iuniStyleRepository.getByMemberId(map.getMemberId(), true);
                IuniStyle data = new IuniStyle();
                data.setId(temp.getId());
                data.setBackground(temp.getBackground());
                data.setBody(temp.getBody());
                data.setLeftWhiteEye(temp.getLeftWhiteEye());
                data.setLeftEye(temp.getLeftEye());
                data.setRightWhiteEye(temp.getRightWhiteEye());
                data.setRightEye(temp.getRightEye());
                data.setNose(temp.getNose());
                result.add(data);
            }
            return result;
        }
        catch (Exception e){
            System.out.println("IuniStyleSerivce_loadJoinMember_error = " + e);
            return new ArrayList<>();
        }
    }

    public IuniStyle getById(Long memberId){
        try{
            JoinIuniStyle temp = this.iuniStyleRepository.getByMemberId(memberId, true);
            if(temp == null){
                return new IuniStyle();
            } else {
                IuniStyle result = new IuniStyle();
                result.setId(temp.getId());
                result.setBackground(temp.getBackground());
                result.setBody(temp.getBody());
                result.setLeftWhiteEye(temp.getLeftWhiteEye());
                result.setLeftEye(temp.getLeftEye());
                result.setRightWhiteEye(temp.getRightWhiteEye());
                result.setRightEye(temp.getRightEye());
                result.setNose(temp.getNose());
                return result;
            }
        }
        catch(Exception e){
            throw e;
        }
    }
    public IuniStyle get(String token) {
        try{
            Long memberId = jwt.getId(token);
            JoinIuniStyle temp = this.iuniStyleRepository.getByMemberId(memberId, true);
            Member member = memberRepository.findById(memberId).orElse(new Member());

            IuniStyle result = new IuniStyle();
            result.setId(temp.getId());
            result.setBackground(temp.getBackground());
            result.setBody(temp.getBody());
            result.setLeftWhiteEye(temp.getLeftWhiteEye());
            result.setLeftEye(temp.getLeftEye());
            result.setRightWhiteEye(temp.getRightWhiteEye());
            result.setRightEye(temp.getRightEye());
            result.setNose(temp.getNose());
            return result;
        }
        catch(Exception e) {
            //throw e;
            System.out.println("IuniStyleService_get_error = " + e);
            return new IuniStyle();
        }
    }

    public Boolean update(IuniStyle data) {
        try{
            System.out.println(data.getId());
            Optional<IuniStyle> optUpdateData = this.iuniStyleRepository.findById(data.getId());
            if(optUpdateData.isPresent()){
                IuniStyle updateData = optUpdateData.get();
                updateData.setBackground(data.getBackground());
                updateData.setBody(data.getBody());
                updateData.setLeftWhiteEye(data.getLeftWhiteEye());
                updateData.setLeftEye(data.getLeftEye());
                updateData.setRightWhiteEye(data.getRightWhiteEye());
                updateData.setRightEye(data.getRightEye());
                updateData.setNose(data.getNose());
                iuniStyleRepository.save(updateData);
                return true;
            } else {
                return false;
            }
        }
        catch(Exception e){
            throw e;
        }

    }
}
