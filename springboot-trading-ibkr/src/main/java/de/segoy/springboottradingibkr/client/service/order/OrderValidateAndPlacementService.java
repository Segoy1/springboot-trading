package de.segoy.springboottradingibkr.client.service.order;

import de.segoy.springboottradingdata.model.data.entity.OrderDbo;
import de.segoy.springboottradingibkr.client.service.ApiCaller;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderValidateAndPlacementService {

    private final UniqueContractDataProvider uniqueContractDataProvider;
    private final @Qualifier("OrderPlacementApiCaller")ApiCaller<OrderDbo> orderPlacementApiCaller;

    public  void validateAndPlaceOrder(OrderDbo orderData) {
        uniqueContractDataProvider.getExistingContractDataOrCallApi(orderData.getContractDBO()).ifPresent(
                (contractData) -> {
                    orderData.setContractDBO(contractData);
                    orderPlacementApiCaller.callApi(orderData);
                });


    }
}
