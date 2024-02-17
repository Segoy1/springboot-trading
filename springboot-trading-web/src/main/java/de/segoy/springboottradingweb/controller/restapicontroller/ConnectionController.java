package de.segoy.springboottradingweb.controller.restapicontroller;

import de.segoy.springboottradingweb.ConnectionInitiator;
import de.segoy.springboottradingweb.SpringbootTradingApplication;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ConnectionController {

    private final ConnectionInitiator connectionInitiator;

    @GetMapping("disconnect")
    public void disconnect(){
        connectionInitiator.disconnect();
    }

    @GetMapping("connect")
    public void connect(){
//        int port = stage.equals("live")? SpringbootTradingApplication.LIVE_TRADING_PORT:SpringbootTradingApplication.DOCKER_TRADING_PORT;
        connectionInitiator.connect(SpringbootTradingApplication.DOCKER_TRADING_PORT);
    }
}
