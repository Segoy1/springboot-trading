package de.segoy.springboottradingweb;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.model.ConnectionData;
import de.segoy.springboottradingdata.model.message.TwsMessage;
import de.segoy.springboottradingdata.repository.ConnectionDataRepository;
import de.segoy.springboottradingdata.repository.message.TwsMessageRepository;
import de.segoy.springboottradingibkr.client.services.EReaderHolder;
import org.springframework.stereotype.Service;

@Service
public class ConnectionInitiator {


    private final Integer LAST_CLIENT_ID = 0;
    private final EClientSocket client;
    private final ConnectionDataRepository connectionDataRepository;
    private final TwsMessageRepository tws_messages;
    private final EReaderHolder eReaderHolder;


    public ConnectionInitiator(EClientSocket m_client, ConnectionDataRepository connectionDataRepository, TwsMessageRepository tws, EReaderHolder eReaderHolder) {
        this.client = m_client;
        this.connectionDataRepository = connectionDataRepository;
        this.tws_messages = tws;
        this.eReaderHolder = eReaderHolder;
    }

    public void connect(int port) {
        if (client.isConnected()) {
            return;
        }

        client.optionalCapabilities("");
        client.eConnect("", port, 0);

        if (client.isConnected()) {
            ConnectionData connectionData = ConnectionData.builder()
                    .m_retIpAddress("")
                    .m_retPort(port)
                    .m_retClientId(0)
                    .m_retOptCapts("")
                    .m_bIsFAAccount(false)
                    .m_disconnectInProgress(false)
                    .build();

            TwsMessage msg = TwsMessage.builder()
                    .message("Connected to Tws server version " +
                            client.serverVersion() + " at " +
                            client.getTwsConnectionTime()).build();
            System.out.println(msg.getMessage());
            tws_messages.save(msg);
            ConnectionData savedConnectionData = connectionDataRepository.save(connectionData);
            eReaderHolder.startReader();
        }
    }

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
