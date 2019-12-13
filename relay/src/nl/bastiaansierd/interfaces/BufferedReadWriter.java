package nl.bastiaansierd.interfaces;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public interface BufferedReadWriter {
    boolean isConnected();
    BufferedReader getReader();
    BufferedWriter getWriter();
}
