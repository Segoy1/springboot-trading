package de.segoy.springboottradingibkr.client.service.position.profitandloss;

import de.segoy.springboottradingdata.model.entity.PositionData;
import de.segoy.springboottradingdata.model.entity.ProfitAndLossData;
import de.segoy.springboottradingdata.service.apiresponsecheck.SinglePnLApiResponseChecker;
import de.segoy.springboottradingibkr.client.service.ApiCaller;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SinglePnLService {

    private final ApiCaller<PositionData> singlePnLApiCaller;
    private final ApiCaller<PositionData> singlePnLCancelApiCaller;
    private final SinglePnLApiResponseChecker singlePnLApiResponseChecker;

    public SinglePnLService(@Qualifier("SinglePnLApiCaller") ApiCaller<PositionData> singlePnLApiCaller,
                            @Qualifier("SinglePnLCancelApiCaller") ApiCaller<PositionData> singlePnLCancelApiCaller, SinglePnLApiResponseChecker singlePnLApiResponseChecker) {
        this.singlePnLApiCaller = singlePnLApiCaller;
        this.singlePnLCancelApiCaller = singlePnLCancelApiCaller;
        this.singlePnLApiResponseChecker = singlePnLApiResponseChecker;
    }

    public Optional<ProfitAndLossData> getProfitAndLossData(PositionData positionData){
//        singlePnLCancelApiCaller.callApi(positionData);
        singlePnLApiCaller.callApi(positionData);
        return Optional.empty();
//        return singlePnLApiResponseChecker.checkForApiResponseAndUpdate(positionData.getContractData().getContractId());
    }
}
