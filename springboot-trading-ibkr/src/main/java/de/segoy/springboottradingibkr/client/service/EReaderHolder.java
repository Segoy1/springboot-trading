package de.segoy.springboottradingibkr.client.service;

import com.ib.client.EClientSocket;
import com.ib.client.EJavaSignal;
import com.ib.client.EReader;
import de.segoy.springboottradingibkr.client.config.PropertiesConfig;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class EReaderHolder {

    private final EJavaSignal signal;
    private final EClientSocket client;
    private final PropertiesConfig propertiesConfig;
    @Getter
    private EReader reader;

    public EReaderHolder(EJavaSignal signal, EClientSocket client, PropertiesConfig propertiesConfig) {
        this.signal = signal;
        this.client = client;
        this.propertiesConfig = propertiesConfig;
    }

    public void startReader() {
        //Don't know why but reader hast to be initialized here and not in Constructor...
        this.reader = new EReader(client, signal);
        reader.start();
        propertiesConfig.setReaderStarted(true);
    }

    @PreDestroy
    private void stopReader() {
        reader.interrupt();
        System.out.println("Reader Stopped: " + reader.getName());

    }
}
