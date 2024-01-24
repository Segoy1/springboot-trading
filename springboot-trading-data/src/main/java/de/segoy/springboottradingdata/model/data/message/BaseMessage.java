package de.segoy.springboottradingdata.model.data.message;

import de.segoy.springboottradingdata.model.data.IBKRDataType;
import jakarta.persistence.Lob;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@MappedSuperclass
@Getter
public abstract class BaseMessage extends IBKRDataType {

    private Integer messageId;

    @Lob
    private String message;

    @CreationTimestamp
    private Timestamp createDate;
}
