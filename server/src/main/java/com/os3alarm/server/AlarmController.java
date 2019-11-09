package com.os3alarm.server;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alarms")
public class AlarmController {

    AlarmService alarmService;

    @Autowired
    public AlarmController(AlarmService alarmService) {this.alarmService = alarmService;}

    @Async
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Alarm> getAll() {return alarmService.findAll();}

    @Async
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public void create(@RequestBody Alarm alarm) {
        alarmService.save(alarm);
    }

    @Async
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void remove(@PathVariable long id) {
        alarmService.deleteById(id);
    }

    @Async
    @PutMapping(value = "/put/{id}")
    public String update(@RequestBody Alarm newAlarm, @PathVariable long id) {
        return alarmService.findById(id)
        .map(oldAlarm -> {
            oldAlarm.setAlarmStatus(newAlarm.getAlarmStatus());
            alarmService.save(oldAlarm);
            return "";
        })
        .orElseGet(() -> {
            return "";
        });
    }




}
