package de.segoy.springboottradingdata.repository;

import de.segoy.springboottradingdata.model.OrderData;
import org.springframework.data.repository.CrudRepository;

public interface OrderDataRepository  extends CrudRepository<OrderData, Integer> {
}
