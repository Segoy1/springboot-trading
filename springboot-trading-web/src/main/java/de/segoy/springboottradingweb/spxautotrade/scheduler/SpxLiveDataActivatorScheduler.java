package de.segoy.springboottradingweb.spxautotrade.scheduler;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.dataobject.ContractDataTemplates;
import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingibkr.client.service.marketdata.AutoTradeMarketDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SpxLiveDataActivatorScheduler {

    private final PropertiesConfig propertiesConfig;
    private final AutoTradeMarketDataService autoTradeMarketDataService;


//    @Scheduled(cron = "0 25 15 * * 1-5")
    @Scheduled(cron = "*/10 * * * * *")
    public void buy() {
        ContractData spx = ContractDataTemplates.SpxData();
        autoTradeMarketDataService.requestLiveMarketDataForContractData(propertiesConfig.getSpxTickerId(), spx);

    }

}
