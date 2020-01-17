package com.os3alarm.server.controllers;

import com.os3alarm.server.components.Authorization;
import com.os3alarm.shared.models.Commands;
import com.os3alarm.server.services.RelayService;
import com.os3alarm.server.services.AlarmService;
import com.os3alarm.server.models.Alarm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private Authorization authorization;

    @Autowired
    public AlarmController(AlarmService alarmService, RelayService relayService, Authorization authorization) {
        this.alarmService = alarmService;
        this.relayService = relayService;
        this.authorization = authorization;
    }

    @Async
    @GetMapping(value = "/getAlarmsFromUser")
    public ArrayList<Alarm> getAllFromUser() {return alarmService.findAllByUser(authorization.getUserName()).get();}

    @Async
    @PostMapping(value = "/sendcommand/{token}")
    public void command(@RequestBody String command, @PathVariable String token){
        if(authorization.isAuthorized(token)){
            relayService.pushCommand(token, Commands.valueOf(command));
        }
    }

    @Async
    @PostMapping(value = "/registerAlarmByToken/{token}")
    public Alarm registerAlarmByToken(@PathVariable String token){
        /// TODO: Check nullable; implement Optional.
        Alarm alarm = alarmService.getAlarmByToken(token).get();

        if(alarm.getUser() == null){
            String user = authorization.getUserName();

            alarm.setUser(user);
            alarmService.save(alarm);
            return alarm;
        }

        return null;
    }

    /// TODO: voor testing - has to be removed in final version
    @Async
    @GetMapping
    public List<Alarm> getAll() {return alarmService.findAll();}
}
