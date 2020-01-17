package com.os3alarm.alarmconnector.controllers;

import com.os3alarm.shared.models.LiveAlarmPool;
import com.os3alarm.alarmconnector.models.LiveAlarm;
import com.os3alarm.alarmconnector.models.RelayStream;
import com.os3alarm.shared.models.AlarmStatus;

public class LiveAlarmController {
    private LiveAlarmPool pool;
    private JsonController jsonController;
    private RelayStream stream;

    public LiveAlarmController(JsonController jsonController, RelayStream stream){
        this.pool = LiveAlarmPool.getInstance();
        this.stream = stream;
        this.jsonController = jsonController;
    }

    public void update(){
        if(this.pool.getAlarmByToken(this.jsonController.getToken()) == null ) {
            createAlarm(this.jsonController.getToken());
        }

        updateAlarm();
    }

    public void setToDisconnected(){
        //Read failed, set connection to disconnected
        System.out.println("Read failed, connection set to disconnected");

        pool.getAlarmByToken(jsonController.getToken()).setWriter(null);
        pool.getAlarmByToken(jsonController.getToken()).setStatus(AlarmStatus.Disconnected);
        stream.clearReader();

    }

    private void createAlarm(String token){
        LiveAlarm alarm = new LiveAlarm(token);
        pool.addAlarm(alarm);
    }

    private void updateAlarm(){
        LiveAlarm liveAlarm = this.pool.getAlarmByToken(jsonController.getToken());

        liveAlarm.update(jsonController.getStatus(), jsonController.getAudioOn(), jsonController.getJsonSensors());
        if(liveAlarm.getWriter() == null && jsonController.getToken() != null){
            liveAlarm.setWriter(stream.getWriter());
        }
    }

}
