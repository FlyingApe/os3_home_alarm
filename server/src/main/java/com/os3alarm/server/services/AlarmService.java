package com.os3alarm.server.services;


import com.os3alarm.server.models.Alarm;
import com.os3alarm.server.repositories.AlarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AlarmService {
    private final AlarmRepository alarmRepository;

    @Autowired
    public AlarmService(AlarmRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
    }

    @Async
    public List<Alarm> findAll() {
        return alarmRepository.findAll();
    }

    @Async
    public Optional<ArrayList<Alarm>> findAllByUser(String user) {
        return alarmRepository.getAlarmsByUser(user);
    }

    @Async
    public void save(Alarm alarm) {
        alarmRepository.save(alarm);
    }

    @Async
    public Optional<Alarm> getAlarmByToken(String token){
        return alarmRepository.getAlarmByToken(token);
    }
}
