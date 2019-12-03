package com.os3alarm.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import org.springframework.messaging.handler.annotation.MessageMapping;

import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/alarm")
public class AlarmController {

    private AlarmService alarmService;

    @Autowired
    public AlarmController(AlarmService alarmService) {this.alarmService = alarmService;}

    @Async
    @GetMapping(value = "all")
    public List<Alarm> getAll() {return alarmService.findAll();}

    @Async
    @GetMapping(value = "{id}")
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
    @DeleteMapping(value="delete/{id}")
    public void remove(@PathVariable int id) {
        alarmService.deleteById(id);
    }

    @Async
    @PutMapping(value = "/{id}")
    /// TODO: Implement future-like return type.
    public String update(@RequestBody Alarm newAlarm, @PathVariable int id) {
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
    }


}
