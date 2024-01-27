package de.segoy.springboottradingdata.service.apiresponsecheck;

import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingdata.repository.IBKRDataTypeRepository;
import de.segoy.springboottradingdata.service.ApiResponseErrorHandler;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class ContractApiResponseChecker implements OptionalApiResponseChecker<ContractData> {

    private final IBKRDataTypeRepository<ContractData> repository;
    private final RepositoryRefreshService repositoryRefreshService;
    private final ApiResponseErrorHandler apiResponseErrorHandler;


    public ContractApiResponseChecker(IBKRDataTypeRepository<ContractData> repository,
                                      RepositoryRefreshService repositoryRefreshService, ApiResponseErrorHandler apiResponseErrorHandler) {
        this.repositoryRefreshService = repositoryRefreshService;
        this.repository = repository;
        this.apiResponseErrorHandler = apiResponseErrorHandler;
    }

    public Optional<ContractData> checkForApiResponseAndUpdate(int id) {
        do{
            repositoryRefreshService.clearCacheAndWait(repository);
        }
        while(notInRepositoryOrError(id));
        return repository.findById((long) id);
    }

    protected boolean notInRepositoryOrError(int id){
        return repository.findById((long)id).isEmpty() && !apiResponseErrorHandler.isErrorForId(id);
    }
}
