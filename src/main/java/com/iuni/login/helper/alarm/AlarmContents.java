package com.iuni.login.helper.alarm;


import lombok.Getter;
import java.util.ArrayList;

@Getter
public class AlarmContents {
    private ArrayList<ResAlarm> alarms;
    private ArrayList<ResAlarm> shareTaskAlarms;

    public void setAlarms(ArrayList<ResAlarm> resAlarms){
        this.alarms = resAlarms;
    }

    public void setShareTaskAlarms(ArrayList<ResAlarm> resAlarms){
        this.shareTaskAlarms = resAlarms;
    }
}
