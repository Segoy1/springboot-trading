package de.segoy.springboottradingibkr.client.service.order.openorder;

import de.segoy.springboottradingdata.model.OrderData;
import de.segoy.springboottradingibkr.client.service.ApiCallerWithoutParameter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpenOrderService {


    private ApiCallerWithoutParameter<OrderData> openOrderApiCaller;

    public OpenOrderService(@Qualifier("OpenOrderApiCaller") ApiCallerWithoutParameter<OrderData> openOrderApiCaller) {
        this.openOrderApiCaller = openOrderApiCaller;
    }

    public List<OrderData> getOpenOrders(){
        openOrderApiCaller.callApi();
        //TODO here I need to build something with Kafka to get the Value!!
        return new ArrayList<>();
    }
}
