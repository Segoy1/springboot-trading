package de.segoy.springboottradingibkr.client.service.accountsummery;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.AccountSummary;
import de.segoy.springboottradingibkr.client.service.ApiCaller;
import de.segoy.springboottradingibkr.client.service.ApiCallerWithoutParameter;
import org.springframework.stereotype.Component;

@Component
public class AccountSummaryApiCaller implements ApiCallerWithoutParameter<AccountSummary> {

    private final EClientSocket client;
    private final PropertiesConfig propertiesConfig;

    //TODO move Values
    public final static String TAGS= "AccruedCash,BuyingPower,NetLiquidation";
    public final static String GROUD_NAME= "All";

    public AccountSummaryApiCaller(EClientSocket client, PropertiesConfig propertiesConfig) {
        this.client = client;
        this.propertiesConfig = propertiesConfig;
    }

    @Override
    public void callApi() {
        client.reqAccountSummary(propertiesConfig.getACCOUNT_SUMMARY_ID(), GROUD_NAME, TAGS);
    }
}
