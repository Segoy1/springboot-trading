package de.segoy.springboottradingdata.repository;

import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.model.PositionData;
import org.springframework.lang.NonNullApi;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PositionDataRepository extends IBKRDataTypeRepository<PositionData> {

    Optional<PositionData> findFirstByContractData(ContractData contractData);

    @Override
    List<PositionData> findAll();
}
