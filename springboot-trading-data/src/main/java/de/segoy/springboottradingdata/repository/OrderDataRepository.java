package de.segoy.springboottradingdata.repository;

import de.segoy.springboottradingdata.model.OrderData;

import java.util.Optional;

public interface OrderDataRepository  extends IBKRDataTypeRepository<OrderData> {

    Optional<OrderData> findTopByOrderByIdDesc();
}
