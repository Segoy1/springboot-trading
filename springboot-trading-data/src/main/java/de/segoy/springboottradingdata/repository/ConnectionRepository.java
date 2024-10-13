package de.segoy.springboottradingdata.repository;

import de.segoy.springboottradingdata.model.data.entity.ConnectionDbo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ConnectionRepository extends CrudRepository<ConnectionDbo, Long> {

    @Modifying
    @Query("update ConnectionDbo c set c.connected = false where c.id = ?1")
    void setConnectFalseById(Long id);
}
