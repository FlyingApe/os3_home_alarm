package com.os3alarm.server.controllers;

import com.os3alarm.server.models.Commands;
import com.os3alarm.server.models.RelayDataObserver;
import com.os3alarm.server.services.RelayService;
import com.os3alarm.server.services.AlarmService;
import com.os3alarm.server.models.Alarm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/alarm")
public class AlarmController {

    private AlarmService alarmService;
    private RelayService relayService;

    @Autowired
    public AlarmController(AlarmService alarmService, RelayService relayService) {
        this.alarmService = alarmService;
        this.relayService = relayService;
    }

    @Async
    @GetMapping(value = "all")
    public List<Alarm> getAll() {return alarmService.findAll();}

    @Async
    @GetMapping(value = "/{id}")
    /// TODO: Implement future-like return type.
    public Alarm get(@PathVariable int id) {
        Optional<Alarm> alarm = alarmService.findById(id);
        if (alarm.isPresent()) {
            return alarm.get();
        } else {
            throw new ResourceNotFoundException();
        }
    }


    @Async
    @PostMapping(value="post")
    public void create(@RequestBody Alarm alarm) {
        alarmService.save(alarm);
    }


    @Async
    @DeleteMapping(value="/delete/{id}")
    public void remove(@PathVariable int id) {
        alarmService.deleteById(id);
    }


    @Async
    @PutMapping(value = "/{id}")
    /// TODO: Implement future-like return type.
    public String update(@RequestBody Alarm newAlarm, @PathVariable int id) {
        /*
        return alarmService.findById(id)
        .map(oldAlarm -> {
            oldAlarm.setStatus(newAlarm.getStatus());
            oldAlarm.setName(newAlarm.getName());
            alarmService.save(oldAlarm);
            return "";
        })
        .orElseGet(() -> {
            return "";
        });

         */

        return "";
    }

    @Async
    @PostMapping(value = "/sendcommand/{token}")
    public void command(@RequestBody String command, @PathVariable String token){
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //String user = authentication.getName();

        relayService.pushCommand(token, Commands.valueOf(command));
    }

    @Async
    @GetMapping(value = "/status/{token}")
    public String status(@PathVariable String token){
        //relayService
        return "";
    }


    @Async
    @GetMapping(value = "/registerAlarmByToken/{token}")
    public void registerAlarmByToken(@PathVariable String token){
        Alarm alarm = alarmService.getAlarmByToken(token).get();

        /// TODO: user uit eigen service halen
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();

        alarm.setUser(user);

        alarmService.save(alarm);
    }


    @Async
    @PostMapping(value="/registerArduino")
    public void arduinoRegister(@RequestBody Alarm alarm) {
        if(!alarmService.getAlarmByToken(alarm.getToken()).isPresent()){
            alarmService.save(alarm);
        }
    }

    /*
    @Async
    @GetMapping(value = "/getbytoken/{token}")
    public Alarm getAlarmByToken(@PathVariable String token){
        Alarm alarm = alarmService.getAlarmByToken(token).get();
        return alarm;
    }
     */

    /*
    @Async
    @MessageMapping(value = "/sensorDataTest/{token}")
    //@SendTo(value = "/sensorDataTest/{token}")
    public String TestSensorData(@PathVariable String token){
        RelayDataObserver observer = new RelayDataObserver();
        relayService.subscribeToJsonSensorDataByToken(token, observer);

        return observer.getSensorData();
    }

     */


  /*
    @Async
    @SendTo(value = "/")
*/

}
