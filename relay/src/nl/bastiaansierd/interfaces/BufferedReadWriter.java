package nl.bastiaansierd.interfaces;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public interface BufferedReadWriter {
    void connect();
    boolean isConnected();
    String getName();
    BufferedReader getReader();
    BufferedWriter getWriter();
    void clearReader();
    void clearWriter();
}
