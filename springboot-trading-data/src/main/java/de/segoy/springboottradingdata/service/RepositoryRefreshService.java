package de.segoy.springboottradingdata.service;

import de.segoy.springboottradingdata.model.entity.IBKRDataTypeEntity;
import de.segoy.springboottradingdata.repository.IBKRDataTypeRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RepositoryRefreshService {


    private final EntityManager entityManager;

    public RepositoryRefreshService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public <T extends IBKRDataTypeEntity> void clearCache(IBKRDataTypeRepository<T> repository){
        entityManager.getEntityManagerFactory().getCache().evict(repository.getClass());
        entityManager.clear();
    }
}
