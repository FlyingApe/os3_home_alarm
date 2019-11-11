package com.os3alarm.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/alarm")
public class AlarmController {

    AlarmService alarmService;

    @Autowired
    public AlarmController(AlarmService alarmService) {this.alarmService = alarmService;}

    @Async
    @RequestMapping(method = RequestMethod.GET)
    public List<Alarm> getAll() {return alarmService.findAll();}

    @Async
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Alarm get(@PathVariable int id) {
        Optional<Alarm> alarm = alarmService.findById(id);
        if (alarm.isPresent()) {
            return alarm.get();
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @Async
    @RequestMapping(method = RequestMethod.POST)
    public void create(@RequestBody Alarm alarm) {
        alarmService.save(alarm);
    }

    @Async
    @RequestMapping(method = RequestMethod.DELETE)
    public void remove(@PathVariable int id) {
        alarmService.deleteById(id);
    }

    @Async
    @PutMapping(value = "/{id}")
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
