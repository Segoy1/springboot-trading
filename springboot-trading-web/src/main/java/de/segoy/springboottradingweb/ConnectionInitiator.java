package de.segoy.springboottradingweb;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.model.entity.ConnectionData;
import de.segoy.springboottradingdata.model.entity.message.TwsMessage;
import de.segoy.springboottradingdata.repository.ConnectionDataRepository;
import de.segoy.springboottradingdata.repository.message.TwsMessageRepository;
import de.segoy.springboottradingibkr.client.service.EReaderHolder;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
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

    public boolean connect(int port) {
        if (client.isConnected()) {
            return true;
        }
        client.eConnect("", port, 0);

        if (client.isConnected()) {
            ConnectionData connectionData = ConnectionData.builder()
                    .ipAddress("")
                    .port(port)
                    .clientId(0)
                    .optCapts("")
                    .isFAAccount(false)
                    .disconnectInProgress(false)
                    .connected(true)
                    .build();

            TwsMessage msg = TwsMessage.builder()
                    .message("Connected to Tws server version " +
                            client.serverVersion() + " at " +
                            client.getTwsConnectionTime()).build();
            log.info(msg.getMessage());
            tws_messages.save(msg);
            ConnectionData savedConnectionData = connectionDataRepository.save(connectionData);
            eReaderHolder.startReader();
            return true;
        }
        return false;
    }

    @PreDestroy
    public ConnectionData disconnect(){
        ConnectionData connectionData = connectionDataRepository.findById(1).orElse(ConnectionData.builder().build());
        connectionData.setDisconnectInProgress(true);
        connectionDataRepository.save(connectionData);

        client.eDisconnect();

        connectionDataRepository.deleteAll();
        return connectionData;
    }
}
