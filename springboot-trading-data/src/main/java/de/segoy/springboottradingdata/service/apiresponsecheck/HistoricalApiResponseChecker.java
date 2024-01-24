package de.segoy.springboottradingdata.service.apiresponsecheck;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.data.entity.HistoricalData;
import de.segoy.springboottradingdata.repository.HistoricalDataRepository;
import de.segoy.springboottradingdata.repository.message.ErrorMessageRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
class HistoricalApiResponseChecker implements ListApiResponseChecker<HistoricalData> {

    private final RepositoryRefreshService repositoryRefreshService;
    private final HistoricalDataRepository repository;
    private final PropertiesConfig propertiesConfig;
    private final ErrorMessageRepository errorMessageRepository;

    public HistoricalApiResponseChecker(RepositoryRefreshService repositoryRefreshService,
                                        HistoricalDataRepository historicalDataRepository,
                                        PropertiesConfig propertiesConfig,
                                        ErrorMessageRepository errorMessageRepository, ErrorMessageRepository errorMessageRepository1) {
        this.repositoryRefreshService = repositoryRefreshService;
        this.repository = historicalDataRepository;
        this.propertiesConfig = propertiesConfig;
        this.errorMessageRepository = errorMessageRepository1;
    }

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
        return repository.findById((long)id).isEmpty() && errorMessageRepository.findAllByMessageIdAndCreateDateIsAfter(id,
                Timestamp.from(Instant.now().minusSeconds(5))).isEmpty();
    }

}
