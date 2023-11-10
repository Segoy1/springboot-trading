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

public class ConnectController {

    private final Integer LIVE_TRADING_PORT = 7496;
    private final Integer PAPER_TRADING_PORT = 7497;
    private final Integer LAST_CLIENT_ID = 0;
    private final EClientSocket m_client;
    private final ConnectionDataRepository connectionDataRepository;
    private final TwsMessageRepository m_TWS;

    public ConnectController(EClientSocket m_client, ConnectionDataRepository connectionDataRepository, TwsMessageRepository m_TWS) {
        this.m_client = m_client;
        this.connectionDataRepository = connectionDataRepository;
        this.m_TWS = m_TWS;
    }

    @RequestMapping("/connect")
    public ConnectionData connect(@RequestParam(defaultValue = "") String ip,
                        @RequestParam(defaultValue = "live") String stage,
                        @RequestParam(defaultValue = "0") int clientId,
                        @RequestParam(defaultValue = "") String optionalCapabilities) {
        if (m_client.isConnected()) {
            return null;//TODO get proper Connection Details
        }
        int port = stage.equals("paper") ? PAPER_TRADING_PORT : LIVE_TRADING_PORT;

        ConnectionData connectionData = ConnectionData.builder()
                .m_retIpAddress(ip)
                .m_retPort(port)
                .m_retClientId(clientId)
                .m_retOptCapts(optionalCapabilities)
                .m_bIsFAAccount(false)
                .m_disconnectInProgress(false)
                .build();



        m_client.optionalCapabilities(connectionData.getM_retOptCapts());
        m_client.eConnect(connectionData.getM_retIpAddress(), connectionData.getM_retPort(), connectionData.getM_retClientId());

        if (m_client.isConnected()) {
            TwsMessage msg = TwsMessage.builder()
                    .message("Connected to Tws server version " +
                            m_client.serverVersion() + " at " +
                            m_client.getTwsConnectionTime()).build();
            System.out.println(msg);
            m_TWS.save(msg);
        }
        return connectionDataRepository.save(connectionData);
    }
}
