package de.segoy.springboottradingdata.service;

import de.segoy.springboottradingdata.model.IBKRDataTypeEntity;
import de.segoy.springboottradingdata.repository.IBKRDataTypeRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RepositoryRefreshService {


    private final EntityManager entityManager;

    public RepositoryRefreshService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public <T extends IBKRDataTypeEntity> void clearCacheAndWait(IBKRDataTypeRepository<T> repository){
        timeOutToWaitForRefresh();
        entityManager.getEntityManagerFactory().getCache().evict(repository.getClass());
        entityManager.clear();
    }

    private void timeOutToWaitForRefresh() {
        try {
            //10ms Time Out before refreshing Cache
            TimeUnit.MILLISECONDS.sleep(100L);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }
}
