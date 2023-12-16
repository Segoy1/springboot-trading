package de.segoy.springboottradingdata.repository;

import de.segoy.springboottradingdata.model.entity.ContractData;
import de.segoy.springboottradingdata.model.entity.PositionData;
import java.util.Optional;

public interface PositionDataRepository extends IBKRDataTypeRepository<PositionData> {

    Optional<PositionData> findFirstByContractData(ContractData contractData);
}
