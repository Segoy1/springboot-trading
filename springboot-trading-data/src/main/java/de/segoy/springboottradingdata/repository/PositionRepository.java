package de.segoy.springboottradingdata.repository;

import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.entity.PositionDbo;
import java.util.Optional;

public interface PositionRepository extends IBKRDataTypeRepository<PositionDbo> {

    Optional<PositionDbo> findFirstByContractDBO(ContractDbo contractDBO);
}
