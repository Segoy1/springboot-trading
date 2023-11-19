package de.segoy.springboottradingibkr.client.services;

import com.ib.client.EClientSocket;
import com.ib.client.EJavaSignal;
import com.ib.client.EReader;
import jakarta.annotation.PreDestroy;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class EReaderThreadHolder {

    private final EJavaSignal signal;
    private final EClientSocket client;
    private EReader reader;
    private final TaskExecutor taskExecutor;

    public EReaderThreadHolder(EJavaSignal signal, EClientSocket client, TaskExecutor taskExecutor) {
        this.signal = signal;
        this.client = client;
        this.taskExecutor = taskExecutor;
    }

    public void startReader() {
        //Dont't know why but reader hast to be initialized here and not in Constructor...
        this.reader = new EReader(client,signal);
        reader.start();

        taskExecutor.execute(() -> {
            processMessages();
            int i = 0;
            System.out.println(i);
        });
    }

    protected void processMessages() {

        while (client.isConnected()) {
            signal.waitForSignal();
            try {
                reader.processMsgs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @PreDestroy
    private void stopThread() {
        reader.interrupt();
        System.out.println("Reader Stopped: " + reader.getName());

    }
}
