package de.segoy.springboottradingibkr.client.aspect;

import com.ib.client.EClientSocket;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;

@Aspect
@Service
@Slf4j
public class ApiCallerAspect {

    private final EClientSocket client;

    public ApiCallerAspect(EClientSocket client) {
        this.client = client;
    }

    @Around("execution(public void callApi(..))")
    protected void checkIfConnected(ProceedingJoinPoint joinPoint) throws Throwable {
        if(client.isConnected()){
            joinPoint.proceed();
        } else{
            throw new RuntimeException("Client is not connected. Open API connection with IBKR!");
        }
    }
}
