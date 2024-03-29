package de.segoy.springboottradingdata.service;

import de.segoy.springboottradingdata.model.data.IBKRDataType;
import de.segoy.springboottradingdata.repository.IBKRDataTypeRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class RepositoryRefreshService {

    private final EntityManager entityManager;


    public <T extends IBKRDataType> void clearCacheAndWait(IBKRDataTypeRepository<T> repository){
        timeOutToWaitForRefresh();
        entityManager.getEntityManagerFactory().getCache().evict(repository.getClass());
        entityManager.clear();
    }
    private void timeOutToWaitForRefresh() {
        try {
            //10ms Time Out before refreshing Cache
            TimeUnit.MILLISECONDS.sleep(10L);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }
}
