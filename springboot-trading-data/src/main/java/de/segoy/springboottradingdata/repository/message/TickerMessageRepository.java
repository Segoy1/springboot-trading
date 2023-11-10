package de.segoy.springboottradingdata.repository.message;

import de.segoy.springboottradingdata.model.message.TickerMessage;
import org.springframework.data.repository.CrudRepository;

public interface TickerMessageRepository extends CrudRepository<TickerMessage, Integer> {
}
