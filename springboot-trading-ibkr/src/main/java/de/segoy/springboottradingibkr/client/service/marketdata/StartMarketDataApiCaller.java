package de.segoy.springboottradingibkr.client.service.marketdata;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingdata.modelconverter.ContractDataToIBKRContract;
import de.segoy.springboottradingibkr.client.service.ApiCaller;
import org.springframework.stereotype.Service;

@Service
class StartMarketDataApiCaller implements ApiCaller<ContractData> {

    private final EClientSocket client;
    private final ContractDataToIBKRContract contractDataToIBKRContract;
    private final PropertiesConfig propertiesConfig;

    public StartMarketDataApiCaller(EClientSocket client, ContractDataToIBKRContract contractDataToIBKRContract, PropertiesConfig propertiesConfig) {
        this.client = client;
        this.contractDataToIBKRContract = contractDataToIBKRContract;
        this.propertiesConfig = propertiesConfig;
    }

    public void callApi(ContractData savedContract) {
        client.reqMktData(savedContract.getContractId(),
                contractDataToIBKRContract.convertContractData(savedContract),
                propertiesConfig.getGenericTicks(),
                false,
                false,
                null);
    }
}
