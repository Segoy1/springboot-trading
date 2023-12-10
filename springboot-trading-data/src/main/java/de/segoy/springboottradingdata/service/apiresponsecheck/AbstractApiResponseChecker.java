package de.segoy.springboottradingdata.service.apiresponsecheck;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.IBKRDataTypeEntity;
import de.segoy.springboottradingdata.repository.IBKRDataTypeRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;

import java.util.Optional;

abstract class AbstractApiResponseChecker<T extends IBKRDataTypeEntity> implements ApiResponseCheckerForOptional<T> {

    private final IBKRDataTypeRepository<T> repository;
    private final RepositoryRefreshService repositoryRefreshService;
    private final PropertiesConfig propertiesConfig;

    public AbstractApiResponseChecker(IBKRDataTypeRepository<T> repository, RepositoryRefreshService repositoryRefreshService, PropertiesConfig propertiesConfig) {
        this.repositoryRefreshService = repositoryRefreshService;
        this.repository = repository;
        this.propertiesConfig = propertiesConfig;
    }


    public Optional<T> checkForApiResponseAndUpdate(int id) {
        do {
            repositoryRefreshService.clearCacheAndWait(repository);
        } while (propertiesConfig.getActiveApiCalls().contains(id));
        return repository.findById((long)id);
    }
}

