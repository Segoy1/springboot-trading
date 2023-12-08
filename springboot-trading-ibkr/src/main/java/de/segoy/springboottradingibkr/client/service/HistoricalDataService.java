package de.segoy.springboottradingibkr.client.service;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.model.HistoricalMarketData;
import de.segoy.springboottradingdata.repository.HistoricalMarketDataRepository;
import de.segoy.springboottradingibkr.client.datamodel.HistoricalDataSettings;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HistoricalDataService {

    private final UniqueContractDataProvider uniqueContractDataProvider;
    private final EClientSocket client;
    private final HistoricalMarketDataRepository historicalMarketDataRepository;

    public HistoricalDataService(UniqueContractDataProvider uniqueContractDataProvider, EClientSocket client, HistoricalMarketDataRepository historicalMarketDataRepository) {
        this.uniqueContractDataProvider = uniqueContractDataProvider;
        this.client = client;
        this.historicalMarketDataRepository = historicalMarketDataRepository;
    }

    public List<HistoricalMarketData> requestHistoricalData(ContractData contractData, HistoricalDataSettings settings) {
        return new ArrayList<>();
    }
}
