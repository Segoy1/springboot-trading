package de.segoy.springboottradingibkr.client.service.position.profitandloss;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.model.entity.PositionData;
import de.segoy.springboottradingibkr.client.service.ApiCaller;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("SinglePnLCancelApiCaller")
class SinglePnLCancelApiCaller implements ApiCaller<PositionData> {
    private final EClientSocket client;

    public SinglePnLCancelApiCaller(EClientSocket client) {
        this.client = client;
    }

    @Override
    public void callApi(PositionData positionData) {
        client.cancelPnLSingle(positionData.getContractData().getContractId());
    }
}
