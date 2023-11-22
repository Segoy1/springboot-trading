package de.segoy.springboottradingweb.aspect;

import com.ib.client.EReader;
import de.segoy.springboottradingibkr.client.services.EReaderHolder;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ReaderAspect {

    private final EReaderHolder eReaderHolder;

    public ReaderAspect(EReaderHolder eReaderHolder) {
        this.eReaderHolder = eReaderHolder;
    }

    @After("bean(eClientSocket)")
    protected void processMessages() {
        if (eReaderHolder.isStarted()) {
            EReader reader = eReaderHolder.getReader();
            try {
                reader.processMsgs();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

        }
    }
}
