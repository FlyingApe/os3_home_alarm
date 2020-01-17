package com.os3alarm.server.components;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.os3alarm.server.services.RelayService;
import com.os3alarm.shared.interfaces.LiveAlarmListener;
import com.os3alarm.shared.models.AlarmStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Date;


@Component
public class LiveAlarmRepresentation extends TextWebSocketHandler implements LiveAlarmListener {
    private final int secondsToKeepAlive = 20;
    private WebSocketSession session;

    private RelayService relayService;
    private String token;
    private Date lastCalled = null;

    private String jsonSensorData;
    private boolean alarmAudioOn;
    private AlarmStatus status;

    public LiveAlarmRepresentation(RelayService relayService){
        this.relayService = relayService;
        this.lastCalled = new Date();
    }

    private void keepAlive(){
        this.lastCalled = new Date();
    }

    @Override
    public void newDataRecieved() {
        this.jsonSensorData = relayService.getLiveAlarmByToken(this.token).getJsonSensorData();
        this.alarmAudioOn = relayService.getLiveAlarmByToken(this.token).isAlarmAudioOn();
        this.status = relayService.getLiveAlarmByToken(this.token).getStatus();

        try {
            this.session.sendMessage(new TextMessage("SensorData " + jsonSensorData));
            keepAlive();
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    @Override
    public boolean isAlive() {
        Date currentDate = new Date();
        boolean isAlive = (currentDate.toInstant().getEpochSecond() < (secondsToKeepAlive + lastCalled.toInstant().getEpochSecond()));
        return isAlive;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        this.session = session;

        String payload = message.getPayload();

        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject = (JsonObject) Jsoner.deserialize(payload);
        } catch (JsonException e) {
            e.printStackTrace();
        }
        String token = jsonObject.getString(Jsoner.mintJsonKey("token", new String()));

        this.token = token;
        relayService.subscribeToAlarmData(token, this);
        keepAlive();

    }


}
