package de.segoy.springboottradingdata.repository.message;

import de.segoy.springboottradingdata.model.data.message.ErrorMessage;
import de.segoy.springboottradingdata.repository.BaseRepository;

import java.sql.Timestamp;
import java.util.List;

public interface ErrorMessageRepository extends BaseRepository<ErrorMessage> {

    public List<ErrorMessage> findAllByMessageId(Integer errorId);

    List<ErrorMessage> findAllByMessageIdAndCreateDateIsAfter(Integer errorId, Timestamp createDate);
}
