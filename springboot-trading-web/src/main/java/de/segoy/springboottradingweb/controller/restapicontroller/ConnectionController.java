package de.segoy.springboottradingweb.controller.restapicontroller;

import de.segoy.springboottradingweb.ConnectionInitiator;
import de.segoy.springboottradingweb.SpringbootTradingApplication;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public void connect(@RequestParam(defaultValue = "paper", name = "stage") String stage){
        int port = stage.equals("live")? SpringbootTradingApplication.LIVE_TRADING_PORT:SpringbootTradingApplication.PAPER_TRADING_PORT;
        connectionInitiator.connect(port);
    }
}
