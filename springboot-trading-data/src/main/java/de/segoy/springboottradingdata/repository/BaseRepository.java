package de.segoy.springboottradingdata.repository;

import de.segoy.springboottradingdata.model.BaseEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends CrudRepository<T, Long> {
}
