package com.os3alarm.server.controllers;

import com.os3alarm.server.components.AlarmAuthorization;
import com.os3alarm.shared.models.Commands;
import com.os3alarm.server.services.RelayService;
import com.os3alarm.server.services.AlarmService;
import com.os3alarm.server.models.Alarm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/alarm")
public class AlarmController {
    private AlarmService alarmService;
    private RelayService relayService;
    private AlarmAuthorization alarmAuthorization;

    @Autowired
    public AlarmController(AlarmService alarmService, RelayService relayService, AlarmAuthorization alarmAuthorization) {
        this.alarmService = alarmService;
        this.relayService = relayService;
        this.alarmAuthorization = alarmAuthorization;
    }

    @Async
    @GetMapping(value = "/getAlarmsFromUser")
    public ArrayList<Alarm> getAllFromUser() {
        Optional<ArrayList<Alarm>> userAlarms = alarmService.findAllByUser(alarmAuthorization.getEncodedAuthentication());
        if(userAlarms.isPresent()){
            return userAlarms.get();
        } else {
            return null;
        }
    }

    @Async
    @PostMapping(value = "/sendcommand/{token}", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public void command(@PathVariable("token") String token, @RequestBody JsonCommand jsonCommand){
        relayService.pushCommand(token, Commands.valueOf(jsonCommand.getCommand()));
    }

    @Async
    @PostMapping(value = "/registerAlarmByToken/{token}")
    public Alarm registerAlarmByToken(@PathVariable String token){
        //System.out.println(token);
        /// TODO: Check nullable; implement Optional.
        if(alarmService.getAlarmByToken(token).isPresent()){
            Alarm alarm = alarmService.getAlarmByToken(token).get();

            if(alarm.getAuthenticationToken() == null){
                String user = alarmAuthorization.getEncodedAuthentication();

                alarm.setAuthtoken(user);
                alarmService.save(alarm);
                return alarm;
            }
        }

        return null;
    }


    /// TODO: voor testing - has to be removed in final version - is not unprotected can be seen by all
    @Async
    @GetMapping
    public List<Alarm> getAll() {return alarmService.findAll();}

    //Class that stores json from client
    public static class JsonCommand{
        private String command;

        public String getCommand() {
            return command;
        }
    }
}
