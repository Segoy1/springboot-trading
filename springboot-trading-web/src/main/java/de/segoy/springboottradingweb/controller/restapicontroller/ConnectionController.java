package de.segoy.springboottradingweb.controller.restapicontroller;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingweb.ConnectionInitiator;
import de.segoy.springboottradingweb.SpringbootTradingApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConnectionController {

    private ConnectionInitiator connectionInitiator;
    private EClientSocket client;

    public ConnectionController(ConnectionInitiator connectionInitiator, EClientSocket client) {
        this.connectionInitiator = connectionInitiator;
        this.client = client;
    }

    @GetMapping("disconnect")
    public void disconnect(){
        client.eDisconnect();;
    }

    @GetMapping("connect")
    public void connect(@RequestParam String stage){
        int port = stage.equals("live")? SpringbootTradingApplication.LIVE_TRADING_PORT:SpringbootTradingApplication.PAPER_TRADING_PORT;
        connectionInitiator.connect(port);
    }
}
