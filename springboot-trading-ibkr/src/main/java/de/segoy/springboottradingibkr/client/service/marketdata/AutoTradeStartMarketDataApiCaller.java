package de.segoy.springboottradingibkr.client.service.marketdata;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingdata.modelconverter.ContractDataToIBKRContract;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
class AutoTradeStartMarketDataApiCaller {

    private final EClientSocket client;
    private final ContractDataToIBKRContract contractDataToIBKRContract;
    private final PropertiesConfig propertiesConfig;

    public void callApiWithId(int id, ContractData savedContract) {
        client.reqMktData(id,
                contractDataToIBKRContract.convertContractData(savedContract),
                propertiesConfig.getGenericTicks(),
                false,
                false,
                null);
    }
}
