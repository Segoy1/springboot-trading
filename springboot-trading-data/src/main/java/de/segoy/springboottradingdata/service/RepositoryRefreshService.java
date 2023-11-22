package de.segoy.springboottradingdata.service;

import jakarta.persistence.EntityManager;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RepositoryRefreshService {


    private final EntityManager entityManager;

    public RepositoryRefreshService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void clearCacheAndWait(CrudRepository repository){
        timeOutToWaitForRefresh();
        entityManager.getEntityManagerFactory().getCache().evict(repository.getClass());
        entityManager.clear();
    }

    private void timeOutToWaitForRefresh() {
        try {
            //10ms Time Out before refreshing Cache
            TimeUnit.MILLISECONDS.sleep(10L);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }
}
