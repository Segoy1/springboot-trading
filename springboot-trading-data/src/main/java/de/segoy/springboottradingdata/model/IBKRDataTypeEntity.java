package de.segoy.springboottradingdata.model;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class IBKRDataTypeEntity extends BaseEntity{

    private boolean touchedByApi;
}
