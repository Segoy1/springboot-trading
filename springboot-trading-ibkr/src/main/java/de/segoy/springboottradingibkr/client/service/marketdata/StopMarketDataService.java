package de.segoy.springboottradingibkr.client.service.marketdata;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.repository.ContractRepository;
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
    private final ContractRepository contractRepository;

    public void stopMarketDataForTickerId(int id) {
        contractRepository.findById((long) id).ifPresent((contractData) -> {
                    stopMarketDataApiCaller.callApi(id);
                }
        );

    }

    //TODO does not work this way anymore maybe
    public List<ContractDbo> stopAllMarketData() {
        List<ContractDbo> active = new ArrayList<>();
        propertiesConfig.getActiveMarketData().forEach((id) -> {
            stopMarketDataApiCaller.callApi(id);
            active.addAll(contractRepository.findAllByContractId(id));
        });
        return active;
    }
}
