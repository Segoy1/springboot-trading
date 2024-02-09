package de.segoy.springboottradingibkr.client.service.historicaldata;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.data.entity.HistoricalData;
import de.segoy.springboottradingdata.repository.HistoricalDataRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;
import de.segoy.springboottradingibkr.client.errorhandling.ApiResponseErrorHandler;
import de.segoy.springboottradingibkr.client.service.ListApiResponseChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class HistoricalApiResponseChecker implements ListApiResponseChecker<HistoricalData> {

    private final RepositoryRefreshService repositoryRefreshService;
    private final HistoricalDataRepository repository;
    private final PropertiesConfig propertiesConfig;
    private final ApiResponseErrorHandler apiResponseErrorHandler;
    
    @Override
    public List<HistoricalData> checkForApiResponseAndUpdate(int id) {
        do{
        repositoryRefreshService.clearCacheAndWait(repository);
        }
        while(notInRepositoryOrError(id));
        //implementaion with 2 seconds ago is not very clean, but response on this call is not very important
        //Main functionality here is to write api response to DB, longest call took less than 2 seconds anyway
        return repository.findAllByContractIdAndCreateDateAfter(id, propertiesConfig.getTwoSecondsAgo());
    }
    protected boolean notInRepositoryOrError(int id){
        return repository.findById((long)id).isEmpty() && !apiResponseErrorHandler.isErrorForId(id);
    }

}
