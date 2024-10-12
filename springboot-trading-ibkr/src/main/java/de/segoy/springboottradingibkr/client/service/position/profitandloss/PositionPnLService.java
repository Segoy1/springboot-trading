package de.segoy.springboottradingibkr.client.service.position.profitandloss;

import de.segoy.springboottradingdata.repository.PositionDataRepository;
import de.segoy.springboottradingibkr.client.service.ApiCallerWithId;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PositionPnLService {

    private final PositionDataRepository positionDataRepository;
    private final @Qualifier("SinglePnLApiCaller")ApiCallerWithId singlePnLApiCaller;
    private final  @Qualifier("SinglePnLCancelApiCaller")ApiCallerWithId singlePnLCancelApiCaller;

    public void getPortfolioPnL(){
        positionDataRepository.findAll().forEach((positionData)->{
            getSinglePnL(positionData.getContractDataDBO().getContractId());
        });
    }
    public void getSinglePnL(int id){
        singlePnLApiCaller.callApi(id);
    }
    public void cancelPortfolioPnL(){
        positionDataRepository.findAll().forEach((positionData)->{
            cancelSinglePnL(positionData.getContractDataDBO().getContractId());
        });
    }
    public void cancelSinglePnL(int id){
        singlePnLCancelApiCaller.callApi(id);
    }

}
