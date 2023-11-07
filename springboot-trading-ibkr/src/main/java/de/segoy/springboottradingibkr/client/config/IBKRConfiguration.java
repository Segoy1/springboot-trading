package de.segoy.springboottradingibkr.client.config;

import com.ib.client.EJavaSignal;
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

}
