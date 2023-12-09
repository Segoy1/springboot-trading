package de.segoy.springboottradingdata.service.apiresponsecheck;

import de.segoy.springboottradingdata.model.IBKRDataTypeEntity;
import de.segoy.springboottradingdata.repository.HistoricalMarketDataRepository;
import de.segoy.springboottradingdata.repository.message.ErrorMessageRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HistoricalMarketDataApiResponseChecker {

    private final HistoricalMarketDataRepository repository;
    private final ErrorMessageRepository errorMessageRepository;
    private final RepositoryRefreshService repositoryRefreshService;

    public HistoricalMarketDataApiResponseChecker(HistoricalMarketDataRepository repository, RepositoryRefreshService repositoryRefreshService, ErrorMessageRepository errorMessageRepository) {
        this.repositoryRefreshService = repositoryRefreshService;
        this.errorMessageRepository = errorMessageRepository;
        this.repository = repository;
    }


    public List<IBKRDataTypeEntity> checkForApiResponseAndUpdate(Integer id) {
        List<IBKRDataTypeEntity> dataList = new ArrayList<>();
        do {
            repositoryRefreshService.clearCacheAndWait(repository);
        } while (fetchResponses(id, dataList).isEmpty());
        return dataList;
    }

    private List<IBKRDataTypeEntity> fetchResponses(Integer id, List<IBKRDataTypeEntity> dataList) {
        if (!repository.findAllByContractId(id).isEmpty()) {
            dataList.addAll(repository.findAllByContractId(id));
        } else if (!errorMessageRepository.findAllByErrorId(id).isEmpty()) {
            dataList.addAll(errorMessageRepository.findAllByErrorId(id));
        }
        return dataList;
    }
}
