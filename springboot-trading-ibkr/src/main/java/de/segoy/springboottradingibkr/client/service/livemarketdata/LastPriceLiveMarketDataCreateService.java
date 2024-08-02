package de.segoy.springboottradingibkr.client.service.livemarketdata;


import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.dataobject.ContractDataTemplates;
import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingdata.model.data.entity.LastPriceLiveMarketData;
import de.segoy.springboottradingdata.repository.LastPriceLiveMarketDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LastPriceLiveMarketDataCreateService {

    private final PropertiesConfig propertiesConfig;
    private final LastPriceLiveMarketDataRepository lastPriceLiveMarketDataRepository;

    public LastPriceLiveMarketData createLiveData(int tickerId, double price) {
        ContractData contractData = null;
        if (tickerId == propertiesConfig.getSpxTickerId()) {
            contractData = ContractDataTemplates.SpxData();
        } else {
            throw new RuntimeException("No, ContractData with tickerId: " + tickerId + " found!");
        }
        LastPriceLiveMarketData lastPriceLiveMarketData =
                LastPriceLiveMarketData.builder().tickerId((long)tickerId).lastPrice(price).contractData(contractData).build();
        return lastPriceLiveMarketDataRepository.save(lastPriceLiveMarketData);

    }
}
