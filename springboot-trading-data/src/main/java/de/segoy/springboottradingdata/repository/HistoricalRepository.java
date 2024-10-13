package de.segoy.springboottradingdata.repository;

import de.segoy.springboottradingdata.model.data.entity.HistoricalDbo;

import java.util.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface HistoricalRepository extends IBKRDataTypeRepository<HistoricalDbo> {

List<HistoricalDbo> findAllByContractId(Integer id);

Optional<HistoricalDbo>findFirstByContractIdAndTimeAndCount(int id, Timestamp time, int count);

List<HistoricalDbo> findAllByContractIdAndCreateDateAfter(int id, Date date);
}
