package de.segoy.springboottradingibkr.client.service.position.profitandloss;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.data.entity.ConnectionData;
import de.segoy.springboottradingdata.repository.ConnectionDataRepository;
import de.segoy.springboottradingibkr.client.service.ApiCallerWithId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("SinglePnLApiCaller")
class SinglePnLApiCaller implements ApiCallerWithId {

    private final EClientSocket client;
    private final ConnectionDataRepository connectionDataRepository;
    private final PropertiesConfig propertiesConfig;


    public SinglePnLApiCaller(EClientSocket client, ConnectionDataRepository connectionDataRepository,
                              PropertiesConfig propertiesConfig) {
        this.client = client;
        this.connectionDataRepository = connectionDataRepository;
        this.propertiesConfig = propertiesConfig;
    }

    @Override
    public void callApi(int id) {
        ConnectionData connectionData =
                connectionDataRepository.findById(propertiesConfig.getConnectionId()).orElseThrow();
        client.reqPnLSingle(id, connectionData.getAccountList(), "", id);
    }
}
