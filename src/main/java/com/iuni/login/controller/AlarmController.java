package com.iuni.login.controller;

import com.iuni.login.domain.Alarm;
import com.iuni.login.helper.alarm.AlarmContents;
import com.iuni.login.helper.alarm.ResAlarm;
import com.iuni.login.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/alarm")
public class AlarmController {
    private final AlarmService alarmService;

    @PostMapping()
    public HashMap<String, String> create(@RequestBody Alarm data, @RequestHeader Map<String, String> headers){
        HashMap<String, String> result = new HashMap<>();
        result.put("result", alarmService.create(data, headers.get("authorization")));
        return result;
    }

    @GetMapping("/load")
    public AlarmContents load(@RequestHeader Map<String, String> headers){
        return alarmService.load(headers.get("authorization"));
    }
    @PatchMapping()
    public HashMap<String, String> updateRead(@RequestBody Alarm data){
        HashMap<String, String> result = new HashMap<>();
        result.put("result", alarmService.updateIsRead(data.getId()));
        return result;
    }
}
