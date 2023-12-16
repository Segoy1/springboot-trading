package de.segoy.springboottradingibkr.client.service.order;

import de.segoy.springboottradingdata.model.entity.OrderData;
import de.segoy.springboottradingdata.service.apiresponsecheck.OptionalApiResponseChecker;
import de.segoy.springboottradingibkr.client.service.ApiCaller;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderValidateAndPlacementService {

    private final UniqueContractDataProvider uniqueContractDataProvider;
    private final ApiCaller<OrderData> orderPlacementApiCaller;
    private final OptionalApiResponseChecker<OrderData> orderDataApiResponseChecker;

    public OrderValidateAndPlacementService(UniqueContractDataProvider uniqueContractDataProvider, @Qualifier("OrderPlacementApiCaller")ApiCaller<OrderData> orderPlacementApiCaller, OptionalApiResponseChecker<OrderData> orderDataApiResponseChecker) {
        this.uniqueContractDataProvider = uniqueContractDataProvider;
        this.orderPlacementApiCaller = orderPlacementApiCaller;
        this.orderDataApiResponseChecker = orderDataApiResponseChecker;
    }

    public Optional<OrderData> validateAndPlaceOrder(OrderData orderData) {
        return uniqueContractDataProvider.getExistingContractDataOrCallApi(
                        orderData.getContractData())
                .map((contractData -> {
                    orderData.setContractData(contractData);
                    orderPlacementApiCaller.callApi(orderData);
                    return orderDataApiResponseChecker.checkForApiResponseAndUpdate(orderData.getId().intValue());
                })).orElseGet(Optional::empty);


    }
}
