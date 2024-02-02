package de.segoy.springboottradingibkr.client.aspect;

import com.ib.client.EReader;
import de.segoy.springboottradingibkr.client.config.IBKRConfiguration;
import de.segoy.springboottradingibkr.client.service.EReaderHolder;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
public class ReaderAspect {

    private final EReaderHolder eReaderHolder;
    private final IBKRConfiguration ibkrConfiguration;


    @After("bean(eClientSocket)")
    protected void processMessages() {
        if (ibkrConfiguration.isReaderStarted()) {
            EReader reader = eReaderHolder.getReader();
            try {
                reader.processMsgs();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

        }
    }
}
