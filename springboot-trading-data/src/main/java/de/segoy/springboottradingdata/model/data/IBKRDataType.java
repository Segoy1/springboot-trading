package de.segoy.springboottradingdata.model.data;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class IBKRDataType extends BaseEntity{
}
