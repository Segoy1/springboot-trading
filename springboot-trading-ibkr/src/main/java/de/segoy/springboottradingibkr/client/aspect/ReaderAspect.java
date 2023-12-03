package de.segoy.springboottradingibkr.client.aspect;

import com.ib.client.EReader;
import de.segoy.springboottradingibkr.client.config.PropertiesConfig;
import de.segoy.springboottradingibkr.client.services.EReaderHolder;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ReaderAspect {

    private final EReaderHolder eReaderHolder;
    private final PropertiesConfig propertiesConfig;

    public ReaderAspect(EReaderHolder eReaderHolder, PropertiesConfig propertiesConfig) {
        this.eReaderHolder = eReaderHolder;
        this.propertiesConfig = propertiesConfig;
    }

    @After("bean(eClientSocket)")
    protected void processMessages() {
        if (propertiesConfig.isReaderStarted()) {
            EReader reader = eReaderHolder.getReader();
            try {
                reader.processMsgs();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

        }
    }
}
