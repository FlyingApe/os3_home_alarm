package com.os3alarm.server.repositories;

import com.os3alarm.server.models.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    @Query(value = "SELECT * FROM alarm as a WHERE a.token = ?1", nativeQuery = true)
    Optional<Alarm> getAlarmByToken(String token);

    @Query(value = "SELECT * FROM alarm as a WHERE a.user = ?1", nativeQuery = true)
    Optional<ArrayList<Alarm>> getAlarmsByUser(String user);
}
