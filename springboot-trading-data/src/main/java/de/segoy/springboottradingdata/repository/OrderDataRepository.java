package de.segoy.springboottradingdata.repository;

import de.segoy.springboottradingdata.model.data.entity.OrderDataDBO;

import java.util.Optional;

public interface OrderDataRepository  extends IBKRDataTypeRepository<OrderDataDBO> {

    Optional<OrderDataDBO> findTopByOrderByIdDesc();
}
