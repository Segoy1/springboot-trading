package de.segoy.springboottradingibkr.client.services;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.Order;
import de.segoy.springboottradingdata.model.OrderData;
import de.segoy.springboottradingdata.modelconverter.ContractDataToIBKRContract;
import de.segoy.springboottradingdata.modelconverter.OrderDataToIBKROrder;
import org.springframework.stereotype.Service;

@Service
public class OrderPlacementService {

    private final UniqueContractDataProvider uniqueContractDataProvider;
    private final ContractDataToIBKRContract contractDataToIBKRContract;
    private final OrderDataToIBKROrder orderDatatoIBKROrder;
    private  final EClientSocket client;

    public OrderPlacementService(UniqueContractDataProvider uniqueContractDataProvider, ContractDataToIBKRContract contractDataToIBKRContract, OrderDataToIBKROrder orderDatatoIBKROrder, EClientSocket client) {
        this.uniqueContractDataProvider = uniqueContractDataProvider;
        this.contractDataToIBKRContract = contractDataToIBKRContract;
        this.orderDatatoIBKROrder = orderDatatoIBKROrder;
        this.client = client;
    }

    public void placeOrder(OrderData orderData){
        Contract contract = contractDataToIBKRContract.convertContractData(orderData.getContractData());
        Order order = orderDatatoIBKROrder.convertOrderData(orderData);
        client.placeOrder(orderData.getId(), contract, order);//Todo some kind of Feedback.

    }
}
