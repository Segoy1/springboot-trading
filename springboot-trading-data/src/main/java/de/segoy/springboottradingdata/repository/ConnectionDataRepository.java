package de.segoy.springboottradingdata.repository;

import de.segoy.springboottradingdata.model.data.entity.ConnectionDataDBO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ConnectionDataRepository extends CrudRepository<ConnectionDataDBO, Long> {

    @Modifying
    @Query("update ConnectionDataDBO c set c.connected = false where c.id = ?1")
    void setConnectFalseById(Long id);
}
