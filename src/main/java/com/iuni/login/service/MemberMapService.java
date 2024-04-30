package com.iuni.login.service;

import com.iuni.login.domain.Member;
import com.iuni.login.domain.MemberMap;
import com.iuni.login.helper.jwt.JWT;
import com.iuni.login.repository.MemberMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service("memberMapService")
public class MemberMapService {
    private final MemberMapRepository memberMapRepository;
    private final JWT jwt;
    @Autowired
    public MemberMapService(MemberMapRepository memberMapRepository, JWT jwt){
        this.memberMapRepository = memberMapRepository;
        this.jwt = jwt;
    }



    //친구 불러오기
    public ArrayList<Member> loadFriends(Long id){
        try{

            //ArrayList<MemberMap> memberMaps = memberMapRepository.findAllMyFrined(id).orElse(new ArrayList<>());
            ArrayList<Member> results = new ArrayList<>();
//            for(MemberMap memberMap : memberMaps){
//                results.add(memberMap.getFriend());
//            }
            return results;
        }
        catch(Exception e){
            System.out.println("MemberMapService_loadFriendsError = " + e);
            throw e;
        }
    }


    public String create(MemberMap data){
        try{
            MemberMap memberMap = memberMapRepository.save(data);
            if(memberMap.getId() == null){
                return "createError";
            } else {
                return "success";
            }
        }
        catch(Exception e){

            System.out.println("MemberMapService-Create-Error: = " + e);
            return "exceptionError";
        }
    }

    //타입 변경
    public String update(MemberMap data){
        try{
            Optional<MemberMap> optData = memberMapRepository.findById(data.getId());
            if(optData.isEmpty()){
                return "notExist";
            }

            MemberMap updateData = optData.get();
            updateData.setType(data.getType());
            memberMapRepository.save(updateData);
            return "success";
        }
        catch(Exception e){
            System.out.println("MemberMapService-Update-Error = " + e);
            return "exceptionError";
        }
    }

    //삭제
    public String remove(Long id){
        try{
            memberMapRepository.deleteById(id);
            return "success";
        }
        catch(Exception e){
            System.out.println("MemberMapService-Remove-Error = " + e);
            return "exceptionError";
        }
    }
}
