package de.segoy.springboottradingdata.model.data.message;

import de.segoy.springboottradingdata.model.data.kafka.KafkaDataType;
import jakarta.persistence.Lob;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass
@Getter
public abstract class BaseMessage extends KafkaDataType {

    private Integer messageId;

    @Lob
    private String message;
}
