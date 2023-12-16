package de.segoy.springboottradingibkr.client.service.order.openorders;

import de.segoy.springboottradingdata.model.OrderData;
import de.segoy.springboottradingibkr.client.service.ApiCallerWithoutParameter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpenOrdersService {


    private final ApiCallerWithoutParameter<OrderData> openOrderApiCaller;

    public OpenOrdersService(@Qualifier("OpenOrdersApiCaller") ApiCallerWithoutParameter<OrderData> openOrderApiCaller) {
        this.openOrderApiCaller = openOrderApiCaller;
    }

    public List<OrderData> getOpenOrders(){
        openOrderApiCaller.callApi();
        //TODO here I need to build something with Kafka to get the Value!!
        return new ArrayList<>();
    }
}
