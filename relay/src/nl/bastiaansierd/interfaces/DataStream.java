package nl.bastiaansierd.interfaces;

import java.io.InputStream;
import java.io.OutputStream;

public interface DataStream {
    boolean isConnected();
    void connect();
    InputStream getInputStream();
    OutputStream getOutputStream();
}
