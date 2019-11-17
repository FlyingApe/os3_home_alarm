package nl.bastiaansierd.mockServer.Streams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RelayStream implements nl.bastiaansierd.interfaces.DataStream {
    Socket relaySocket = null;
    private InputStream in = null;
    private OutputStream out = null;

    public RelayStream(Socket s) {
        relaySocket = s;
        connect();
    }

    @Override
    public void connect() {
        try {
            in = relaySocket.getInputStream();
            out = relaySocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isConnected() {
        if (in != null){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public InputStream getInputStream() {
        return in;
    }

    @Override
    public OutputStream getOutputStream() {
        return out;
    }
}
