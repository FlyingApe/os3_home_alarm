package com.os3alarm.server;


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlarmService {

    private final AlarmRepository alarmRepository;

    public AlarmService(AlarmRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
    }

    public List<Alarm> findAll() {
        return alarmRepository.findAll();
    }

    public Optional<Alarm> findById(Long id) {
        return alarmRepository.findById(id);
    }

    public void save(Alarm alarm) {
        alarmRepository.save(alarm);
    }

    public void deleteById(Long id) {
        alarmRepository.deleteById(id);
    }




}
