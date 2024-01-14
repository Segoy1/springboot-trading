package de.segoy.springboottradingibkr.client.service.accountsummary.profitandloss;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.repository.ConnectionDataRepository;
import de.segoy.springboottradingibkr.client.service.ApiCallerWithoutParameter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
@Qualifier("AccountPnLApiCaller")
class AccountPnLApiCaller implements ApiCallerWithoutParameter {

    private final EClientSocket client;
    private final ConnectionDataRepository connectionDataRepository;
    private final PropertiesConfig propertiesConfig;

    public AccountPnLApiCaller(EClientSocket client, ConnectionDataRepository connectionDataRepository,
                               PropertiesConfig propertiesConfig) {
        this.client = client;
        this.connectionDataRepository = connectionDataRepository;
        this.propertiesConfig = propertiesConfig;
    }

    @Override
    public void callApi() {
        connectionDataRepository.findById(propertiesConfig.getConnectionId()).ifPresent(
                (connectionData) -> {
            client.reqPnL(propertiesConfig.getPnlAccountId(), connectionData.getAccountList(), "");
        });
    }
}
