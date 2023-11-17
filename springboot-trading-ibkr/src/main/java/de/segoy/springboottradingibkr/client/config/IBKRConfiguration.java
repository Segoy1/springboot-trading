package de.segoy.springboottradingibkr.client.config;

import com.ib.client.EClientSocket;
import com.ib.client.EJavaSignal;
import de.segoy.springboottradingibkr.client.IBKRConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("de.segoy.springboottradingibkr")
public class IBKRConfiguration {

    @Bean
    public EJavaSignal eJavaSignal(){
        return new EJavaSignal();
    }

    @Bean
    public EClientSocket eClientSocket(EJavaSignal eJavaSignal, IBKRConnection ibkrConnection) {
        return new EClientSocket(ibkrConnection,eJavaSignal);
    }

}