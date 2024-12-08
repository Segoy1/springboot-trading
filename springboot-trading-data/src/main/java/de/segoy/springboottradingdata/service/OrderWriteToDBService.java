package de.segoy.springboottradingdata.service;

import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderStatus;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.entity.OrderDbo;
import de.segoy.springboottradingdata.modelsynchronize.ContractDataDatabaseSynchronizer;
import de.segoy.springboottradingdata.modelconverter.IBKRToOrderDbo;
import de.segoy.springboottradingdata.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.OptionalLong;

@Service
@RequiredArgsConstructor
public class OrderWriteToDBService {

    private final OrderRepository orderRepository;
    private final IBKRToOrderDbo ibkrToOrderDbo;
    private final ContractDataDatabaseSynchronizer contractDataDatabaseSynchronizer;



    public OrderDbo saveOrUpdateFullOrderDataToDb(Order order, Contract contract, String orderStatus) {
        ContractDbo contractDBO = contractDataDatabaseSynchronizer.findInDBOrConvertAndSaveOrUpdateIfIdIsProvided(
                OptionalLong.empty(), contract);
        OrderDbo orderData = ibkrToOrderDbo.convertOrder(order);
        orderData.setStatus(OrderStatus.get(orderStatus));
        orderData.setContractDBO(contractDBO);
        orderData.setLastModifiedBeforeSave();
        return orderRepository.save(orderData);
    }
}
