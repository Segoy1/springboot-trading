package de.segoy.springboottradingibkr.client.service.accountsummary.profitandloss;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.repository.ConnectionDataRepository;
import de.segoy.springboottradingibkr.client.service.ApiCallerWithoutParameter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service("AccountPnLApiCaller")
@RequiredArgsConstructor
class AccountPnLApiCaller implements ApiCallerWithoutParameter {

    private final EClientSocket client;
    private final ConnectionDataRepository connectionDataRepository;
    private final PropertiesConfig propertiesConfig;

    @Override
    public void callApi() {
        connectionDataRepository.findById(propertiesConfig.getConnectionId()).ifPresent(
                (connectionData) -> {
            client.reqPnL(propertiesConfig.getPnlAccountId(), connectionData.getAccountList(), "");
        });
    }
}
