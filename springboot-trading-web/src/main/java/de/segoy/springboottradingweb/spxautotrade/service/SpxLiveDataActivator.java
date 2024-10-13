package de.segoy.springboottradingweb.spxautotrade.service;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.dataobject.ContractDataTemplates;
import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingibkr.client.service.marketdata.AutoTradeMarketDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SpxLiveDataActivator {

    private final PropertiesConfig propertiesConfig;
    private final AutoTradeMarketDataService autoTradeMarketDataService;


    public void getLiveMarketDataSPX() {
        ContractDataDBO spx = ContractDataTemplates.SpxData();
        autoTradeMarketDataService.requestLiveMarketDataForContractData(propertiesConfig.getSpxTickerId(), spx);

    }

}
