package de.segoy.springboottradingweb.controller.restapicontroller;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.model.ConnectionData;
import de.segoy.springboottradingdata.model.message.TwsMessage;
import de.segoy.springboottradingdata.repository.ConnectionDataRepository;
import de.segoy.springboottradingdata.repository.message.TwsMessageRepository;
import de.segoy.springboottradingibkr.client.services.EReaderThreadHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class ConnectionController {

    private final Integer LIVE_TRADING_PORT = 7496;
    private final Integer PAPER_TRADING_PORT = 7497;
    private final Integer LAST_CLIENT_ID = 0;
    private final EClientSocket client;
    private final ConnectionDataRepository connectionDataRepository;
    private final TwsMessageRepository tws_messages;
    private final EReaderThreadHolder eReaderThreadHolder;


    public ConnectionController(EClientSocket m_client, ConnectionDataRepository connectionDataRepository, TwsMessageRepository tws, EReaderThreadHolder eReaderThreadHolder) {
        this.client = m_client;
        this.connectionDataRepository = connectionDataRepository;
        this.tws_messages = tws;
        this.eReaderThreadHolder = eReaderThreadHolder;
    }

    @RequestMapping("/connect")
    public ResponseEntity<ConnectionData> connect(@RequestParam(defaultValue = "",name = "ip") String ip,
                                                 @RequestParam(defaultValue = "live", name = "stage") String stage,
                                                 @RequestParam(defaultValue = "0", name="clientId") int clientId,
                                                 @RequestParam(defaultValue = "",name= "optCap") String optCap) {
        if (client.isConnected()) {
            return ResponseEntity.ok(connectionDataRepository.findAll().iterator().next());//TODO figure out what to do with multiple Connections, wich shouldnt happen anyway
        }

        int port = stage.equals("paper") ? PAPER_TRADING_PORT : LIVE_TRADING_PORT;

        client.optionalCapabilities(optCap);
        client.eConnect(ip, port, clientId);

        if (client.isConnected()) {
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
                            client.serverVersion() + " at " +
                            client.getTwsConnectionTime()).build();
            System.out.println(msg.getMessage());
            tws_messages.save(msg);
            eReaderThreadHolder.startReader();

        return ResponseEntity.ok(connectionDataRepository.save(connectionData));
        }
        return ResponseEntity.badRequest().build();
    }

    @RequestMapping("/disconnect")
    public ConnectionData disconnect(){
        //TODO cleaner implementation for closing connection
        ConnectionData connectionData = connectionDataRepository.findById(1).orElse(ConnectionData.builder().build());
        connectionData.setM_disconnectInProgress(true);
        connectionDataRepository.save(connectionData);

        client.eDisconnect();

        connectionDataRepository.deleteAll();
        return connectionData;
    }
}
