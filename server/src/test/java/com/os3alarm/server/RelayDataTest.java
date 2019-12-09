package com.os3alarm.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.os3alarm.server.models.RelayDataObservable;
import com.os3alarm.server.models.RelayDataObserver;
import org.junit.jupiter.api.Test;


class RelayDataTest {

    private RelayDataObservable observable = new RelayDataObservable();
    private RelayDataObserver observer = new RelayDataObserver();

    @Test
    void setReceived() {
        observable.addPropertyChangeListener(observer);
        observable.setReceived("testValue");

        assertEquals(observer.getReceived(), "testValue");
    }
}
