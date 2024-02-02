package de.segoy.springboottradingibkr.client.aspect;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Service;

@Aspect
@Service
@RequiredArgsConstructor
public class ActiveMarketDataAspect {

    private final PropertiesConfig propertiesConfig;

    @Before("bean(eClientSocket) && execution(* *(int, ..)) && args(id,..) && execution(* reqMktData(int, ..))")
    protected void addToActiveMarketData( int id){
        propertiesConfig.addToActiveMarketData(id);
    }

//    @Before("bean(eClientSocket) && execution(* *(int, ..)) && args(id,..) && execution(* cancelMktData(int, ..))")
//    protected void removeFromActiveMarketData( int id){
//        propertiesConfig.removeFromActiveMarketData(id);
//    }
}
