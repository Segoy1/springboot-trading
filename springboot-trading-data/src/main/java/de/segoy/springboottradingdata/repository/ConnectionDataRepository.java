package de.segoy.springboottradingdata.repository;

import de.segoy.springboottradingdata.model.ConnectionData;
import org.springframework.data.repository.CrudRepository;

public interface ConnectionDataRepository extends CrudRepository<ConnectionData, Integer> {
}
