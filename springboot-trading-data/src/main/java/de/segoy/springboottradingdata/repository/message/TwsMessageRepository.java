package de.segoy.springboottradingdata.repository.message;

import de.segoy.springboottradingdata.model.message.TwsMessage;
import org.springframework.data.repository.CrudRepository;

public interface  TwsMessageRepository extends CrudRepository<TwsMessage, Integer> {
}
