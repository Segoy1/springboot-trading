package de.segoy.springboottradingibkr.client.services;

import com.ib.client.EClientSocket;
import com.ib.client.EJavaSignal;
import com.ib.client.EReader;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class EReaderHolder {

    private final EJavaSignal signal;
    private final EClientSocket client;
    private EReader reader;
    private boolean isStarted;

    public EReaderHolder(EJavaSignal signal, EClientSocket client) {
        this.signal = signal;
        this.client = client;
    }

    public void startReader() {
        //Don't know why but reader hast to be initialized here and not in Constructor...
        this.reader = new EReader(client, signal);
        reader.start();
        isStarted = true;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public EReader getReader() {
        return reader;
    }

    @PreDestroy
    private void stopReader() {
        reader.interrupt();
        System.out.println("Reader Stopped: " + reader.getName());

    }
}
