package de.segoy.springboottradingdata.repository;

import de.segoy.springboottradingdata.model.ContractData;
import org.springframework.data.repository.CrudRepository;

public interface ContractDataRepository  extends CrudRepository<ContractData, Integer> {
}
