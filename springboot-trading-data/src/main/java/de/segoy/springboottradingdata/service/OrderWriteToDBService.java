package de.segoy.springboottradingdata.service;

import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderStatus;
import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.model.OrderData;
import de.segoy.springboottradingdata.modelconverter.ContractDataDatabaseSynchronizer;
import de.segoy.springboottradingdata.modelconverter.IBKROrderToOrderData;
import de.segoy.springboottradingdata.repository.OrderDataRepository;
import org.springframework.stereotype.Service;

import java.util.OptionalLong;

@Service
public class OrderWriteToDBService {
    private final OrderDataRepository orderDataRepository;
    private final IBKROrderToOrderData ibkrOrderToOrderData;
    private final ContractDataDatabaseSynchronizer contractDataDatabaseSynchronizer;

    public OrderWriteToDBService(OrderDataRepository orderDataRepository, IBKROrderToOrderData ibkrOrderToOrderData, ContractDataDatabaseSynchronizer contractDataDatabaseSynchronizer) {
        this.orderDataRepository = orderDataRepository;
        this.ibkrOrderToOrderData = ibkrOrderToOrderData;
        this.contractDataDatabaseSynchronizer = contractDataDatabaseSynchronizer;
    }

    public void saveOrUpdateFullOrderDataToDb(Order order, Contract contract, String orderStatus) {
        ContractData contractData = contractDataDatabaseSynchronizer.findInDBOrConvertAndSaveOrUpdateIfIdIsProvided(OptionalLong.empty(), contract);
        OrderData orderData = ibkrOrderToOrderData.convertOrder(order);
        orderData.setStatus(OrderStatus.get(orderStatus));

        orderData.setContractData(contractData);
        orderDataRepository.save(orderData);

    }
}
