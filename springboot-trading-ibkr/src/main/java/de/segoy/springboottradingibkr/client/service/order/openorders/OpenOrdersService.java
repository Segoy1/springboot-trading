package de.segoy.springboottradingibkr.client.service.order.openorders;

import de.segoy.springboottradingdata.model.entity.OrderData;
import de.segoy.springboottradingdata.service.apiresponsecheck.noinput.OpenOrdersApiResponseChecker;
import de.segoy.springboottradingibkr.client.service.ApiCallerWithoutParameter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenOrdersService {


    private final ApiCallerWithoutParameter<OrderData> openOrderApiCaller;
    private final OpenOrdersApiResponseChecker openOrdersApiResponseChecker;

    public OpenOrdersService(@Qualifier("OpenOrdersApiCaller") ApiCallerWithoutParameter<OrderData> openOrderApiCaller, OpenOrdersApiResponseChecker openOrdersApiResponseChecker) {
        this.openOrderApiCaller = openOrderApiCaller;
        this.openOrdersApiResponseChecker = openOrdersApiResponseChecker;
    }

    public List<OrderData> getOpenOrders(){
        openOrderApiCaller.callApi();
        return openOrdersApiResponseChecker.checkForApiResponseAndUpdate();
    }
}
