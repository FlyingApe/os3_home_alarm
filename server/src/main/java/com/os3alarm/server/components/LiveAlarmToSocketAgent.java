package com.os3alarm.server.components;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.os3alarm.server.services.RelayService;
import com.os3alarm.shared.interfaces.LiveAlarmListener;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;


@Controller

public class LiveAlarmToSocketAgent extends TextWebSocketHandler implements LiveAlarmListener {
    private final int secondsToKeepAlive = 20;
    private WebSocketSession session;
    private String userAuthToken;

    private RelayService relayService;
    private String token = null;
    private Date lastCalled;
    private Lock sessionLock;

    public LiveAlarmToSocketAgent(RelayService relayService){
        this.relayService = relayService;
        this.lastCalled = new Date();
        sessionLock = new ReentrantLock();
    }

    private void keepAlive(){
        this.lastCalled = new Date();
    }


    @Override
    public void newDataRecieved() {
        String allJsonData = relayService.getLiveAlarmByToken(this.token, this.userAuthToken).getAllJsonData();

        sessionLock.lock();
        try {
            this.session.sendMessage(new TextMessage(allJsonData));
            keepAlive();
        } catch (IOException e) {
            //e.printStackTrace();
        } finally {
            sessionLock.unlock();
        }
    }

    @Override
    public boolean isAlive() {
        Date currentDate = new Date();
        return (currentDate.toInstant().getEpochSecond() < (secondsToKeepAlive + lastCalled.toInstant().getEpochSecond()));
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String jsonPostString;
        sessionLock.lock();
        try{
            this.session = session;
            this.userAuthToken = session.getHandshakeHeaders().get("authorization").toString().replaceAll(Pattern.quote("[")+"Basic", "").replaceAll(Pattern.quote("]"), "").trim();
            jsonPostString = message.getPayload();
        } finally {
            sessionLock.unlock();
        }
        JsonObject jsonObject = null;

        String oldToken = token;
        String newToken = null;

        //unscribe to past alarmData
        if(token != null){
            relayService.unscribeToAlarmData(oldToken, this);
        }

        //get new token from message
        try {
            jsonObject = (JsonObject) Jsoner.deserialize(jsonPostString);
            newToken = jsonObject.getString(Jsoner.mintJsonKey("token", new String()));
            this.token = newToken;
        } catch (JsonException e) {
            e.printStackTrace();
        }

        //subscrive to new AlarmData
        relayService.subscribeToAlarmData(newToken, this.userAuthToken, this);

        //keep this object alive
        keepAlive();
    }


}
