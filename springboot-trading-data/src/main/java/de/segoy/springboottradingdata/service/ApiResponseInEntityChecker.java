package de.segoy.springboottradingdata.service;

import de.segoy.springboottradingdata.model.IBKRDataTypeEntity;
import de.segoy.springboottradingdata.repository.IBKRDataTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class ApiResponseInEntityChecker {

    private final RepositoryRefreshService repositoryRefreshService;

    public ApiResponseInEntityChecker(RepositoryRefreshService repositoryRefreshService) {
        this.repositoryRefreshService = repositoryRefreshService;
    }

    public <T extends IBKRDataTypeEntity>T checkForApiResponse(IBKRDataTypeRepository<T> repository, Integer id) {
        T entity;
        do {
            repositoryRefreshService.clearCacheAndWait(repository);
            entity = repository.findById(id).orElseThrow();
        }while(!entity.isTouchedByApi());
            return entity;
        }
}
