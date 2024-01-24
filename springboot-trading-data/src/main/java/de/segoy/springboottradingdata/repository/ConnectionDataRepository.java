package de.segoy.springboottradingdata.repository;

import de.segoy.springboottradingdata.model.data.entity.ConnectionData;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ConnectionDataRepository extends CrudRepository<ConnectionData, Long> {

    @Modifying
    @Query("update ConnectionData c set c.connected = false where c.id = ?1")
    void setConnectFalseById(Long id);
}
