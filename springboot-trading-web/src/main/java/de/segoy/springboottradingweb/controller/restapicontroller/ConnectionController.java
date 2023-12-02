package de.segoy.springboottradingweb.controller.restapicontroller;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.repository.ConnectionDataRepository;
import de.segoy.springboottradingweb.ConnectionInitiator;
import de.segoy.springboottradingweb.SpringbootTradingApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConnectionController {

    private final ConnectionInitiator connectionInitiator;
    private final EClientSocket client;
    private final ConnectionDataRepository connectionDataRepository;

    public ConnectionController(ConnectionInitiator connectionInitiator, EClientSocket client, ConnectionDataRepository connectionDataRepository) {
        this.connectionInitiator = connectionInitiator;
        this.client = client;
        this.connectionDataRepository = connectionDataRepository;
    }

    @GetMapping("disconnect")
    public void disconnect(){
        client.eDisconnect();;
        connectionDataRepository.deleteAll();
    }

    @GetMapping("connect")
    public void connect(@RequestParam String stage){
        int port = stage.equals("live")? SpringbootTradingApplication.LIVE_TRADING_PORT:SpringbootTradingApplication.PAPER_TRADING_PORT;
        connectionInitiator.connect(port);
    }
}
