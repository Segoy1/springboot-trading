package de.segoy.springboottradingweb;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.data.entity.ConnectionDbo;
import de.segoy.springboottradingdata.model.data.message.TwsMessage;
import de.segoy.springboottradingdata.repository.ConnectionRepository;
import de.segoy.springboottradingibkr.client.service.EReaderHolder;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionInitiator {

  private final Integer LAST_CLIENT_ID = 0;
  private final EClientSocket client;
  private final ConnectionRepository connectionRepository;
  private final EReaderHolder eReaderHolder;
  private final PropertiesConfig propertiesConfig;

  public void connect(int port) {
    if (client.isConnected()) {
      return;
    }
    client.eConnect("", port, 0);

    if (client.isConnected()) {
      ConnectionDbo connectionDBO =
          ConnectionDbo.builder()
              .id(propertiesConfig.getConnectionId())
              .ipAddress("")
              .port(port)
              .clientId(0)
              .optCapts("")
              .isFAAccount(false)
              .disconnectInProgress(false)
              .connected(true)
              .build();

      TwsMessage msg =
          TwsMessage.builder()
              .message(
                  "Connected to Tws server version "
                      + client.serverVersion()
                      + " at "
                      + client.getTwsConnectionTime())
              .build();
      log.info(msg.getMessage());
      connectionRepository.save(connectionDBO);
      eReaderHolder.startReader();
    } else {
      throw new RuntimeException("Connection to Tws Failed!!");
    }
  }

  @PreDestroy
  public ConnectionDbo disconnect() {
    ConnectionDbo connectionDBO =
        connectionRepository
            .findById(propertiesConfig.getConnectionId())
            .orElse(ConnectionDbo.builder().build());
    connectionDBO.setDisconnectInProgress(true);
    ConnectionDbo processingConnectionDbo = connectionRepository.save(connectionDBO);

    client.eDisconnect();

    processingConnectionDbo.setConnected(false);
    processingConnectionDbo.setDisconnectInProgress(false);
    return connectionRepository.save(processingConnectionDbo);
  }
}
