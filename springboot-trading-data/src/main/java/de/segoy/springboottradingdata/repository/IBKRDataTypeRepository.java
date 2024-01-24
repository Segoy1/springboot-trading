package de.segoy.springboottradingdata.repository;

import de.segoy.springboottradingdata.model.data.IBKRDataType;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface IBKRDataTypeRepository<T extends IBKRDataType> extends BaseRepository<T> {
}
