package de.segoy.springboottradingibkr.client.aspect;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Service;

@Aspect
@Service
public class ActiveApiCallsAspect {

    private final PropertiesConfig propertiesConfig;

    public ActiveApiCallsAspect(PropertiesConfig propertiesConfig) {
        this.propertiesConfig = propertiesConfig;
    }

//    @Before("bean(eClientSocket) && execution(* *(int, ..)) && args(id,..) && !execution(* *MktData(int, ..))")
//    protected void addToActiveApiCalls( int id){
//        propertiesConfig.addToActiveApiCalls(id);
//    }
}
