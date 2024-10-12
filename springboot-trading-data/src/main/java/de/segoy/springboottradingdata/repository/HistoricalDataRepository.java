package de.segoy.springboottradingdata.repository;

import de.segoy.springboottradingdata.model.data.entity.HistoricalDataDBO;

import java.util.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface HistoricalDataRepository extends IBKRDataTypeRepository<HistoricalDataDBO> {

List<HistoricalDataDBO> findAllByContractId(Integer id);

Optional<HistoricalDataDBO>findFirstByContractIdAndTimeAndCount(int id, Timestamp time, int count);

List<HistoricalDataDBO> findAllByContractIdAndCreateDateAfter(int id, Date date);
}
