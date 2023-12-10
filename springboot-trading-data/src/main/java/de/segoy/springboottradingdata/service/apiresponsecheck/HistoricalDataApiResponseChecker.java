package de.segoy.springboottradingdata.service.apiresponsecheck;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.HistoricalData;
import de.segoy.springboottradingdata.repository.HistoricalDataRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoricalDataApiResponseChecker {

    private final HistoricalDataRepository repository;
    private final RepositoryRefreshService repositoryRefreshService;
    private final PropertiesConfig propertiesConfig;

    public HistoricalDataApiResponseChecker(HistoricalDataRepository repository, RepositoryRefreshService repositoryRefreshService, PropertiesConfig propertiesConfig) {
        this.repositoryRefreshService = repositoryRefreshService;
        this.repository = repository;
        this.propertiesConfig = propertiesConfig;
    }


    public List<HistoricalData> checkForApiResponseAndUpdate(Integer id) {
        do {
            repositoryRefreshService.clearCacheAndWait(repository);
        } while (propertiesConfig.getActiveApiCalls().contains(id));
        return repository.findAllByContractId(id);
    }
}
