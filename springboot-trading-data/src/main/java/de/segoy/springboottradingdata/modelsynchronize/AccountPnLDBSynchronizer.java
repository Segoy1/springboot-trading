package de.segoy.springboottradingdata.modelsynchronize;

import de.segoy.springboottradingdata.model.entity.ProfitAndLossData;
import de.segoy.springboottradingdata.repository.ProfitAndLossDataRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountPnLDBSynchronizer {

    private final ProfitAndLossDataRepository profitAndLossDataRepository;

    public AccountPnLDBSynchronizer(ProfitAndLossDataRepository profitAndLossDataRepository) {
        this.profitAndLossDataRepository = profitAndLossDataRepository;
    }

    public void saveToDB(ProfitAndLossData profitAndLossData){
        if(profitAndLossData.getRealizedPnL()==Double.MAX_VALUE){
            profitAndLossData.setRealizedPnL(null);
        }
        if(profitAndLossData.getDailyPnL()==Double.MAX_VALUE){
            profitAndLossData.setDailyPnL(null);
        }
        if(profitAndLossData.getUnrealizedPnL()==Double.MAX_VALUE){
            profitAndLossData.setUnrealizedPnL(null);
        }
        profitAndLossDataRepository.save(profitAndLossData);
    }
}
