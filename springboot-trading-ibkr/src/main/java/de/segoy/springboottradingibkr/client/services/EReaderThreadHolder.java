package de.segoy.springboottradingibkr.client.services;

import com.ib.client.EClientSocket;
import com.ib.client.EJavaSignal;
import com.ib.client.EReader;
import org.springframework.stereotype.Component;

@Component
public class EReaderThreadHolder {

    private final EJavaSignal signal;
    private final EClientSocket client;
    private EReader reader;

    public EReaderThreadHolder(EJavaSignal signal, EClientSocket client) {
        this.signal = signal;
        this.client = client;
//        this.reader = new EReader(client,signal);
    }

    public void startReader(){
        reader = new EReader(client,signal);
        reader.start();

        new Thread(() -> {
            processMessages();

            int i = 0;
            System.out.println(i);
        }).start();
    }
    private void processMessages() {

        while (client.isConnected()) {
            signal.waitForSignal();
            try {
                reader.processMsgs();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
