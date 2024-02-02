package de.segoy.springboottradingibkr.client.service.marketdata;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import de.segoy.springboottradingibkr.client.service.ApiCallerWithId;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StopMarketDataService {

    private final PropertiesConfig propertiesConfig;
    private final @Qualifier("StopMarketDataApiCaller") ApiCallerWithId stopMarketDataApiCaller;
    private final ContractDataRepository contractDataRepository;

    public void stopMarketDataForContractId(int id) {
        contractDataRepository.findById((long) id).ifPresent((contractData) -> {
                    stopMarketDataApiCaller.callApi(id);
                }
        );

    }

    //TODO does not work this way anymore maybe
    public List<ContractData> stopAllMarketData() {
        List<ContractData> active = new ArrayList<>();
        propertiesConfig.getActiveMarketData().forEach((id) -> {
            stopMarketDataApiCaller.callApi(id);
            active.addAll(contractDataRepository.findAllByContractId(id));
        });
        return active;
    }
}
