package de.segoy.springboottradingweb.controller.restapicontroller;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.model.ConnectionData;
import de.segoy.springboottradingdata.model.message.TwsMessage;
import de.segoy.springboottradingdata.repository.ConnectionDataRepository;
import de.segoy.springboottradingdata.repository.message.TwsMessageRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class ConnectionController {

    private final Integer LIVE_TRADING_PORT = 7496;
    private final Integer PAPER_TRADING_PORT = 7497;
    private final Integer LAST_CLIENT_ID = 0;
    private final EClientSocket m_client;
    private final ConnectionDataRepository connectionDataRepository;
    private final TwsMessageRepository m_TWS;

    public ConnectionController(EClientSocket m_client, ConnectionDataRepository connectionDataRepository, TwsMessageRepository m_TWS) {
        this.m_client = m_client;
        this.connectionDataRepository = connectionDataRepository;
        this.m_TWS = m_TWS;
    }

    @RequestMapping("/connect")
    public ConnectionData connect(@RequestParam(defaultValue = "",name = "ip") String ip,
                        @RequestParam(defaultValue = "live", name = "stage") String stage,
                        @RequestParam(defaultValue = "0", name="clientId") int clientId,
                        @RequestParam(defaultValue = "",name= "optCap") String optCap) {
        if (m_client.isConnected()) {
            return connectionDataRepository.findAll().iterator().next();//Todo Return proper Value when multiple connections are stored
        }

        int port = stage.equals("paper") ? PAPER_TRADING_PORT : LIVE_TRADING_PORT;

        m_client.optionalCapabilities(optCap);
        m_client.eConnect(ip, port, clientId);

        if (m_client.isConnected()) {
            ConnectionData connectionData = ConnectionData.builder()
                    .m_retIpAddress(ip)
                    .m_retPort(port)
                    .m_retClientId(clientId)
                    .m_retOptCapts(optCap)
                    .m_bIsFAAccount(false)
                    .m_disconnectInProgress(false)
                    .build();

            TwsMessage msg = TwsMessage.builder()
                    .message("Connected to Tws server version " +
                            m_client.serverVersion() + " at " +
                            m_client.getTwsConnectionTime()).build();
            System.out.println(msg.getMessage());
            m_TWS.save(msg);
        return connectionDataRepository.save(connectionData);
        }
        return null; //Todo check how to properly return nothing in rest
    }

    @RequestMapping("/disconnect")
    public ConnectionData disconnect(){
        //TODO cleaner implementation for closing connection
        ConnectionData connectionData = connectionDataRepository.findById(1).orElse(ConnectionData.builder().build());
        connectionData.setM_disconnectInProgress(true);
        connectionDataRepository.save(connectionData);

        m_client.eDisconnect();

        connectionDataRepository.deleteAll();
        return connectionData;
    }
}
