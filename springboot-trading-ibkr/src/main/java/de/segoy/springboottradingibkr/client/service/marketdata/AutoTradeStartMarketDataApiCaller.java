package de.segoy.springboottradingibkr.client.service.marketdata;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.modelconverter.ContractDboToIBKRContract;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
class AutoTradeStartMarketDataApiCaller {

    private final EClientSocket client;
    private final ContractDboToIBKRContract contractDboToIBKRContract;
    private final PropertiesConfig propertiesConfig;

    public void callApiWithId(int id, ContractDbo savedContract) {
        client.reqMktData(id,
                contractDboToIBKRContract.convertContractData(savedContract),
                propertiesConfig.getGenericTicks(),
                false,
                false,
                null);
    }
}
