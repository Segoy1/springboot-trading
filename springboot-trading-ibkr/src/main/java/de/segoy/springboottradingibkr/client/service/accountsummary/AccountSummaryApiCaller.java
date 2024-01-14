package de.segoy.springboottradingibkr.client.service.accountsummary;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.entity.AccountSummaryData;
import de.segoy.springboottradingibkr.client.service.ApiCallerWithoutParameter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("AccountSummaryApiCaller")
public class AccountSummaryApiCaller implements ApiCallerWithoutParameter<AccountSummaryData> {

    private final EClientSocket client;
    private final PropertiesConfig propertiesConfig;

    public final static String GROUD_NAME = "All";

    public AccountSummaryApiCaller(EClientSocket client, PropertiesConfig propertiesConfig) {
        this.client = client;
        this.propertiesConfig = propertiesConfig;
    }

    @Override
    public void callApi() {
        String tags =
                propertiesConfig.getACCRUED_CASH() + "," +
                        propertiesConfig.getBUYING_POWER() + "," +
                        propertiesConfig.getNET_LIQUIDATION();
        client.reqAccountSummary(propertiesConfig.getACCOUNT_SUMMARY_ID(), GROUD_NAME, tags);
    }
}
