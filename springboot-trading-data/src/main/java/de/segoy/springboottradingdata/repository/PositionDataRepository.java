package de.segoy.springboottradingdata.repository;

import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.model.data.entity.PositionDataDBO;
import java.util.Optional;

public interface PositionDataRepository extends IBKRDataTypeRepository<PositionDataDBO> {

    Optional<PositionDataDBO> findFirstByContractDataDBO(ContractDataDBO contractDataDBO);
}
