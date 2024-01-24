package de.segoy.springboottradingibkr.client.service.marketdata;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.entity.database.ContractData;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StopMarketDataService {

    private final PropertiesConfig propertiesConfig;
    private final StopMarketDataApiCaller stopMarketDataApiCaller;
    private final ContractDataRepository contractDataRepository;

    public StopMarketDataService(PropertiesConfig propertiesConfig, StopMarketDataApiCaller stopMarketDataApiCaller, ContractDataRepository contractDataRepository) {
        this.propertiesConfig = propertiesConfig;
        this.stopMarketDataApiCaller = stopMarketDataApiCaller;
        this.contractDataRepository = contractDataRepository;
    }

    public Optional<ContractData> stopMarketDataForContractId(int id) {
        return contractDataRepository.findFirstByContractId(id).map((contractData -> {
            stopMarketDataApiCaller.callApi(id);
            return Optional.of(contractData);
        })).orElse(Optional.empty());

    }

    public List<ContractData> stopAllMarketData() {
        List<ContractData> active = new ArrayList<>();
        propertiesConfig.getActiveMarketData().forEach((id) -> {
            stopMarketDataApiCaller.callApi(id);
            active.addAll(contractDataRepository.findAllByContractId(id));
        });
        return active;
    }
}
