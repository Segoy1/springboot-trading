package de.segoy.springboottradingibkr.client.service.order;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.Order;
import de.segoy.springboottradingdata.model.data.entity.OrderData;
import de.segoy.springboottradingdata.modelconverter.ContractDataToIBKRContract;
import de.segoy.springboottradingdata.modelconverter.OrderDataToIBKROrder;
import de.segoy.springboottradingibkr.client.service.ApiCaller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("OrderPlacementApiCaller")
@RequiredArgsConstructor
class OrderPlacementApiCaller implements ApiCaller<OrderData> {

    private final ContractDataToIBKRContract contractDataToIBKRContract;
    private final OrderDataToIBKROrder orderDatatoIBKROrder;
    private final EClientSocket client;

    public void callApi(OrderData orderData) {
        Contract contract = contractDataToIBKRContract.convertContractData(orderData.getContractData());
        Order order = orderDatatoIBKROrder.convertOrderData(orderData);

        client.placeOrder(orderData.getId().intValue(), contract, order);
    }
}
