package de.segoy.springboottradingdata.repository;

import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingdata.model.data.entity.PositionData;
import java.util.Optional;

public interface PositionDataRepository extends IBKRDataTypeRepository<PositionData> {

    Optional<PositionData> findFirstByContractData(ContractData contractData);
}
