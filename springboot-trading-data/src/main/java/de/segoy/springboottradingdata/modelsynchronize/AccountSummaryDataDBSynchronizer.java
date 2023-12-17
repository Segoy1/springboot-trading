package de.segoy.springboottradingdata.modelsynchronize;

import de.segoy.springboottradingdata.model.entity.AccountSummaryData;
import de.segoy.springboottradingdata.repository.AccountSummaryDataRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountSummaryDataDBSynchronizer {

    private final AccountSummaryDataRepository accountSummaryDataRepository;

    public AccountSummaryDataDBSynchronizer(AccountSummaryDataRepository accountSummaryDataRepository) {
        this.accountSummaryDataRepository = accountSummaryDataRepository;
    }

    public void sendAccountSummaryMessage(AccountSummaryData accountSummaryData){
        accountSummaryData.setId(accountSummaryData.determineIdByTag());
        accountSummaryDataRepository.save(accountSummaryData);
    }

}
