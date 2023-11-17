package de.segoy.springboottradingibkr.client.services;

import de.segoy.springboottradingdata.modelconverter.ContractDataToIBKRContract;
import de.segoy.springboottradingdata.modelconverter.OrderDataToIBKROrder;
import org.springframework.stereotype.Service;

@Service
public class OrderExecutionService {

    private final ContractDataToIBKRContract contractDataToIBKRContract;
    private final OrderDataToIBKROrder orderDatatoIBKROrder;

    public OrderExecutionService(ContractDataToIBKRContract contractDataToIBKRContract, OrderDataToIBKROrder orderDatatoIBKROrder) {
        this.contractDataToIBKRContract = contractDataToIBKRContract;
        this.orderDatatoIBKROrder = orderDatatoIBKROrder;
    }
}
