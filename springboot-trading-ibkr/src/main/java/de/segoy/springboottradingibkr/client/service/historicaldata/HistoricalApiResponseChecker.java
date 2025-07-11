package de.segoy.springboottradingibkr.client.service.historicaldata;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.data.entity.HistoricalDbo;
import de.segoy.springboottradingdata.repository.HistoricalRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;
import de.segoy.springboottradingibkr.client.errorhandling.ApiResponseErrorHandler;
import de.segoy.springboottradingibkr.client.service.ListApiResponseChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class HistoricalApiResponseChecker implements ListApiResponseChecker<HistoricalDbo> {

    private final RepositoryRefreshService repositoryRefreshService;
    private final HistoricalRepository repository;
    private final PropertiesConfig propertiesConfig;
    private final ApiResponseErrorHandler apiResponseErrorHandler;
    
    @Override
    public List<HistoricalDbo> checkForApiResponseAndUpdate(int id) {
        do{
        repositoryRefreshService.clearCacheAndWait(repository);
        }
        while(notInRepositoryOrError(id));
        //implementaion with 2 seconds ago is not very clean, but response on this call is not very important
        //Main functionality here is to write api response to DB, longest call took less than 2 seconds anyway
        return repository.findAllByContractIdAndLastModifiedDate(id, propertiesConfig.getFiveSecondsAgo());
    }
    protected boolean notInRepositoryOrError(int id){
        return repository.findAllByContractId(id).isEmpty() && !apiResponseErrorHandler.isErrorForId(id);
    }

}
