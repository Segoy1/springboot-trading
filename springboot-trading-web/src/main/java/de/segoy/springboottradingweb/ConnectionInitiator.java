package de.segoy.springboottradingweb;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.entity.database.ConnectionData;
import de.segoy.springboottradingdata.model.entity.message.TwsMessage;
import de.segoy.springboottradingdata.repository.ConnectionDataRepository;
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
    private final EReaderHolder eReaderHolder;
    private final PropertiesConfig propertiesConfig;


    public ConnectionInitiator(EClientSocket m_client, ConnectionDataRepository connectionDataRepository, EReaderHolder eReaderHolder, PropertiesConfig propertiesConfig) {
        this.client = m_client;
        this.connectionDataRepository = connectionDataRepository;
        this.eReaderHolder = eReaderHolder;
        this.propertiesConfig = propertiesConfig;
    }

    public void connect(int port) {
        if (client.isConnected()) {
            return;
        }
        client.eConnect("", port, 0);

        if (client.isConnected()) {
            ConnectionData connectionData = ConnectionData.builder()
                    .id(propertiesConfig.getConnectionId())
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
            connectionDataRepository.save(connectionData);
            eReaderHolder.startReader();
        }else{
            throw new RuntimeException("Connection to Tws Failed!!");
        }
    }

    @PreDestroy
    public ConnectionData disconnect(){
        ConnectionData connectionData =
                connectionDataRepository.findById(propertiesConfig.getConnectionId()).orElse(ConnectionData.builder().build());
        connectionData.setDisconnectInProgress(true);
        ConnectionData processingConnectionData = connectionDataRepository.save(connectionData);

        client.eDisconnect();

        processingConnectionData.setConnected(false);
        processingConnectionData.setDisconnectInProgress(false);
        return connectionDataRepository.save(processingConnectionData);

    }
}
