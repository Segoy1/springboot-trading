package de.segoy.springboottradingibkr.client.service.order;

import de.segoy.springboottradingdata.model.entity.database.OrderData;
import de.segoy.springboottradingibkr.client.service.ApiCaller;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class OrderValidateAndPlacementService {

    private final UniqueContractDataProvider uniqueContractDataProvider;
    private final ApiCaller<OrderData> orderPlacementApiCaller;

    public OrderValidateAndPlacementService(UniqueContractDataProvider uniqueContractDataProvider,
                                            @Qualifier("OrderPlacementApiCaller") ApiCaller<OrderData> orderPlacementApiCaller) {
        this.uniqueContractDataProvider = uniqueContractDataProvider;
        this.orderPlacementApiCaller = orderPlacementApiCaller;
    }

    public  void validateAndPlaceOrder(OrderData orderData) {
        uniqueContractDataProvider.getExistingContractDataOrCallApi(orderData.getContractData()).ifPresent(
                (contractData) -> {
                    orderData.setContractData(contractData);
                    orderPlacementApiCaller.callApi(orderData);
                });


    }
}
