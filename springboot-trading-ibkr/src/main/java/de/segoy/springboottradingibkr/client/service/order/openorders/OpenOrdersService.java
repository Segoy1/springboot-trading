package de.segoy.springboottradingibkr.client.service.order.openorders;

import de.segoy.springboottradingdata.model.entity.OrderData;
import de.segoy.springboottradingdata.repository.OrderDataRepository;
import de.segoy.springboottradingdata.service.apiresponsecheck.noinput.OpenOrdersApiResponseChecker;
import de.segoy.springboottradingibkr.client.service.ApiCallerWithoutParameter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenOrdersService {


    private final ApiCallerWithoutParameter openOrderApiCaller;
    private final OpenOrdersApiResponseChecker openOrdersApiResponseChecker;
    private final OrderDataRepository orderDataRepository;

    public OpenOrdersService(@Qualifier("OpenOrdersApiCaller") ApiCallerWithoutParameter openOrderApiCaller, OpenOrdersApiResponseChecker openOrdersApiResponseChecker, OrderDataRepository orderDataRepository) {
        this.openOrderApiCaller = openOrderApiCaller;
        this.openOrdersApiResponseChecker = openOrdersApiResponseChecker;
        this.orderDataRepository = orderDataRepository;
    }

    public List<OrderData> getOpenOrders(){
        orderDataRepository.deleteAll();
        //clear any Lag with following line maybe. TODO: clean this code up
//        openOrdersApiResponseChecker.checkForApiResponseAndUpdate();
        openOrderApiCaller.callApi();
        return openOrdersApiResponseChecker.checkForApiResponseAndUpdate();
    }
}
