package de.segoy.springboottradingibkr.client.service.position.profitandloss;

import de.segoy.springboottradingdata.repository.PositionDataRepository;
import de.segoy.springboottradingibkr.client.service.ApiCallerWithId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class PositionPnLService {

    private final PositionDataRepository positionDataRepository;
    private final ApiCallerWithId singlePnLApiCaller;
    private final ApiCallerWithId singlePnLCancelApiCaller;

    public PositionPnLService(PositionDataRepository positionDataRepository,
                              @Qualifier("SinglePnLApiCaller") ApiCallerWithId singlePnLApiCaller,
                              @Qualifier("SinglePnLCancelApiCaller") ApiCallerWithId singlePnLCancelApiCaller) {
        this.positionDataRepository = positionDataRepository;
        this.singlePnLApiCaller = singlePnLApiCaller;
        this.singlePnLCancelApiCaller = singlePnLCancelApiCaller;
    }

    public void getPortfolioPnL(){
        positionDataRepository.findAll().forEach((positionData)->{
            getSinglePnL(positionData.getContractData().getContractId());
        });
    }
    public void getSinglePnL(int id){
        singlePnLApiCaller.callApi(id);
    }
    public void cancelPortfolioPnL(){
        positionDataRepository.findAll().forEach((positionData)->{
            cancelSinglePnL(positionData.getContractData().getContractId());
        });
    }
    public void cancelSinglePnL(int id){
        singlePnLCancelApiCaller.callApi(id);
    }

}
