package de.segoy.springboottradingibkr.client.service.position.profitandloss;

import de.segoy.springboottradingdata.repository.PositionRepository;
import de.segoy.springboottradingibkr.client.service.ApiCallerWithId;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PositionPnLService {

    private final PositionRepository positionRepository;
    private final @Qualifier("SinglePnLApiCaller")ApiCallerWithId singlePnLApiCaller;
    private final  @Qualifier("SinglePnLCancelApiCaller")ApiCallerWithId singlePnLCancelApiCaller;

    public void getPortfolioPnL(){
        positionRepository.findAll().forEach((positionData)->{
            getSinglePnL(positionData.getContractDBO().getContractId());
        });
    }
    public void getSinglePnL(int id){
        singlePnLApiCaller.callApi(id);
    }
    public void cancelPortfolioPnL(){
        positionRepository.findAll().forEach((positionData)->{
            cancelSinglePnL(positionData.getContractDBO().getContractId());
        });
    }
    public void cancelSinglePnL(int id){
        singlePnLCancelApiCaller.callApi(id);
    }

}
