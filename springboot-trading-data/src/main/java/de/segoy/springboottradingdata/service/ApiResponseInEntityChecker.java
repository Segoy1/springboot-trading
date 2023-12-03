package de.segoy.springboottradingdata.service;

import de.segoy.springboottradingdata.model.IBKRDataTypeEntity;
import de.segoy.springboottradingdata.repository.IBKRDataTypeRepository;
import de.segoy.springboottradingdata.repository.message.ErrorMessageRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApiResponseInEntityChecker {

    private final RepositoryRefreshService repositoryRefreshService;
    private final ErrorMessageRepository errorMessageRepository;

    public ApiResponseInEntityChecker(RepositoryRefreshService repositoryRefreshService, ErrorMessageRepository errorMessageRepository) {
        this.repositoryRefreshService = repositoryRefreshService;
        this.errorMessageRepository = errorMessageRepository;
    }

    public <T extends IBKRDataTypeEntity> Optional<T> checkForApiResponseAndUpdate(IBKRDataTypeRepository<T> repository, Integer id) {
        do {
            repositoryRefreshService.clearCacheAndWait(repository);
        } while (repository.findById(id).isEmpty() && errorMessageRepository.findAllByErrorId(id).isEmpty());
        //TODO: If the error Id gets ambiguous for whatever reason: include check by timestamp older than x
        return repository.findById(id);
    }
}
