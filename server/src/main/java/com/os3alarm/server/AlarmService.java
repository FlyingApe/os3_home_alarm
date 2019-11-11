package com.os3alarm.server;


import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.persistence.Convert;
import java.util.List;
import java.util.Optional;

@Service
public class AlarmService {

    private final AlarmRepository alarmRepository;

    public AlarmService(AlarmRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
    }

    @Async
    public List<Alarm> findAll() {
        return alarmRepository.findAll();
    }

    @Async
    public Optional<Alarm> findById(int id) {
        Long _id = (long)id;
        return alarmRepository.findById(_id);
    }

    @Async
    public void save(Alarm alarm) {
        alarmRepository.save(alarm);
    }

    @Async
    public void deleteById(int id) {
        alarmRepository.deleteById((long)id);
    }




}
