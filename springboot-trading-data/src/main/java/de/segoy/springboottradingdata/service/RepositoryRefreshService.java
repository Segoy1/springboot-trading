package de.segoy.springboottradingdata.service;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RepositoryRefreshService {


    private final EntityManager entityManager;

    public RepositoryRefreshService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void clearCacheAndWait(){
        timeStopToWaitForAPI();
        entityManager.clear();
    }

    //TODO find cleaner solution to wait for API
    private void timeStopToWaitForAPI() {
        try {
            System.out.println("Time Stop waiting for API");
            TimeUnit.MILLISECONDS.sleep(50);

        } catch (InterruptedException e) {
            System.out.println("Wow this is ugly and needs a proper Fix");
        }
    }
}
