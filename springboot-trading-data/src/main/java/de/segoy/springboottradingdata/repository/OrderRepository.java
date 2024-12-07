package de.segoy.springboottradingdata.repository;

import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.entity.OrderDbo;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends IBKRDataTypeRepository<OrderDbo> {

    Optional<OrderDbo> findTopByOrderByIdDesc();
    List<OrderDbo> findByContractDBO(ContractDbo contractDbo);
}
