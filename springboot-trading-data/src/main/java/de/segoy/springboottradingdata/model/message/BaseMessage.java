package de.segoy.springboottradingdata.model.message;

import de.segoy.springboottradingdata.model.IBKRDataTypeEntity;
import jakarta.persistence.Lob;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@MappedSuperclass
@Getter
public abstract class BaseMessage extends IBKRDataTypeEntity {

    private Integer messageId;

    @Lob
    private String message;

    @CreationTimestamp
    private Timestamp createDate;
}
