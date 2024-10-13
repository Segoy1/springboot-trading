package de.segoy.springboottradingibkr.client.service.position.profitandloss;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.data.entity.ConnectionDbo;
import de.segoy.springboottradingdata.repository.ConnectionRepository;
import de.segoy.springboottradingibkr.client.service.ApiCallerWithId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("SinglePnLApiCaller")
@RequiredArgsConstructor
class SinglePnLApiCaller implements ApiCallerWithId {

    private final EClientSocket client;
    private final ConnectionRepository connectionRepository;
    private final PropertiesConfig propertiesConfig;

    @Override
    public void callApi(int id) {
        ConnectionDbo connectionDBO =
                connectionRepository.findById(propertiesConfig.getConnectionId()).orElseThrow();
        client.reqPnLSingle(id, connectionDBO.getAccountList(), "", id);
    }
}
