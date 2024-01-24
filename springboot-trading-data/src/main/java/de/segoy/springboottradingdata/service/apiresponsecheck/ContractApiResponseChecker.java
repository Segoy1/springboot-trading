package de.segoy.springboottradingdata.service.apiresponsecheck;

import de.segoy.springboottradingdata.model.entity.ContractData;
import de.segoy.springboottradingdata.repository.IBKRDataTypeRepository;
import de.segoy.springboottradingdata.repository.message.ErrorMessageRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Service
class ContractApiResponseChecker implements OptionalApiResponseChecker<ContractData> {

    private final IBKRDataTypeRepository<ContractData> repository;
    private final RepositoryRefreshService repositoryRefreshService;
    private final ErrorMessageRepository errorMessageRepository;

    public ContractApiResponseChecker(IBKRDataTypeRepository<ContractData> repository,
                                      RepositoryRefreshService repositoryRefreshService, ErrorMessageRepository errorMessageRepository) {
        this.repositoryRefreshService = repositoryRefreshService;
        this.repository = repository;
        this.errorMessageRepository = errorMessageRepository;
    }

    public Optional<ContractData> checkForApiResponseAndUpdate(int id) {
        do{
            repositoryRefreshService.clearCacheAndWait(repository);
        }
        while(notInRepositoryOrError(id));
        return repository.findById((long) id);
    }

    protected boolean notInRepositoryOrError(int id){
        return repository.findById((long)id).isEmpty() && errorMessageRepository.findAllByMessageIdAndCreateDateIsAfter(id,
                Timestamp.from(Instant.now().minusSeconds(5))).isEmpty();
    }
}
