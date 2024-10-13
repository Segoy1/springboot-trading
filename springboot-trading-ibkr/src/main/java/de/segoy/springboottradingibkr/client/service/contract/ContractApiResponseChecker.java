package de.segoy.springboottradingibkr.client.service.contract;

import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.repository.IBKRDataTypeRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;
import de.segoy.springboottradingibkr.client.errorhandling.ApiResponseErrorHandler;
import de.segoy.springboottradingibkr.client.service.OptionalApiResponseChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class ContractApiResponseChecker implements OptionalApiResponseChecker<ContractDbo> {

    private final IBKRDataTypeRepository<ContractDbo> repository;
    private final RepositoryRefreshService repositoryRefreshService;
    private final ApiResponseErrorHandler apiResponseErrorHandler;

    @Transactional
    public Optional<ContractDbo> checkForApiResponseAndUpdate(int id) {
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
