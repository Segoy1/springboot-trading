package de.segoy.springboottradingdata.repository;

import de.segoy.springboottradingdata.model.data.entity.OrderDbo;

import java.util.Optional;

public interface OrderRepository extends IBKRDataTypeRepository<OrderDbo> {

    Optional<OrderDbo> findTopByOrderByIdDesc();
}
