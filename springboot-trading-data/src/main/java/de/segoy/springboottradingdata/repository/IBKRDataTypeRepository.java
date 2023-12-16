package de.segoy.springboottradingdata.repository;

import de.segoy.springboottradingdata.model.entity.IBKRDataTypeEntity;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface IBKRDataTypeRepository<T extends IBKRDataTypeEntity> extends BaseRepository<T> {
}
