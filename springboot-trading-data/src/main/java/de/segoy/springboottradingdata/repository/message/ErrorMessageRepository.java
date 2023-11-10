package de.segoy.springboottradingdata.repository.message;

import de.segoy.springboottradingdata.model.message.ErrorMessage;
import org.springframework.data.repository.CrudRepository;

public interface ErrorMessageRepository extends CrudRepository<ErrorMessage, Integer> {
}
