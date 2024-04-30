package com.iuni.login.service;

import com.iuni.login.domain.Alarm;
import com.iuni.login.domain.Member;
import com.iuni.login.helper.alarm.AlarmContents;
import com.iuni.login.helper.alarm.ResAlarm;
import com.iuni.login.helper.jwt.JWT;
import com.iuni.login.repository.AlarmRepository;
import com.iuni.login.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("alarmService")
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;

    private final JWT jwt;

    private ArrayList<ResAlarm> makeResAlarms(List<Alarm> dataList){
        ArrayList<ResAlarm> result = new ArrayList<ResAlarm>();
        for(Alarm data : dataList){
            ResAlarm resAlarm = new ResAlarm();
            resAlarm.setName(data.getName());
            resAlarm.setId(data.getId());
            resAlarm.setAlarmType(data.getAlarmType());
            resAlarm.setToMember(data.getToMember().makeToMember());
            resAlarm.setFromMember(data.getFromMember().makeFromMember());
            resAlarm.setCreateDate(data.getCreateDate());
            resAlarm.setIsRead(data.getIsRead());
            resAlarm.setRelateId(data.getRelateId());
            result.add(resAlarm);
        }
        return result;
    }

    public AlarmContents load(String token){
        try{
            Member member = memberRepository.findById(jwt.getId(token)).orElse(new Member());
            ArrayList<Alarm> dataList = alarmRepository.findAllByToMember(member).orElse(new ArrayList<>());

            AlarmContents result = new AlarmContents();
            result.setAlarms(makeResAlarms(dataList.stream().filter(t -> t.getAlarmType().equals("NORMAL")).toList()));
            result.setShareTaskAlarms(makeResAlarms(dataList.stream().filter(t -> t.getAlarmType().equals("TASK")).toList()));
            return result;
        }
        catch(Exception e){
            System.out.println("AlarmService_Error = " + e);
            return new AlarmContents();
        }
    }

    //읽음 처리
    public String updateIsRead(Long id){
        try{
            Alarm updateData = alarmRepository.findById(id).orElse(new Alarm());
            updateData.setIsRead(true);
            System.out.println("updateData.getId() = " + updateData.getId());
            alarmRepository.save(updateData);
            return "success";
        }
        catch(Exception e){
            System.out.println("AlarmService_updateIsRead_error = " + e);
            return "fail";
        }
    }

    public String create(Alarm data, String token){
        try{
            if(data.getAlarmType().equals("TASK")){
                Member fromMember = memberRepository.findById(jwt.getId(token)).orElse(new Member());
                String name = fromMember.getNickName() + "(" + fromMember.getEmail() + ") 님이 " + "[ " +data.getName()+ " ] 태스크 공유하기를 요청 했어요.";
                data.setName(name);
                data.setCreateDate(new Date());
                data.setToMember(this.memberRepository.findById(data.getMemberId()).orElse(new Member()));
                data.setFromMember(fromMember);
                alarmRepository.save(data);
                return "success";
            }
            else if(data.getAlarmType().equals("NORMAL")){
                Member fromMember = memberRepository.findById(jwt.getId(token)).orElse(new Member());
                if(data.getName().equals("JOIN")){
                    data.setName(fromMember.getNickName() + "(" + fromMember.getEmail() + ") 님이 태스크 공유를 승인 했어요.");
                } else {
                    data.setName(fromMember.getNickName() + "(" + fromMember.getEmail() + ") 님이 태스크 공유를 거절 했어요.");
                }
                data.setCreateDate(new Date());
                data.setToMember(this.memberRepository.findById(data.getMemberId()).orElse(new Member()));
                data.setFromMember(fromMember);
                alarmRepository.save(data);
                return "success";
            }
            else {
                return "otherType";
            }
        }
        catch(Exception e){
            System.out.println("AlarmService_createError = " + e);
            return "serviceError";
        }
    }
}
