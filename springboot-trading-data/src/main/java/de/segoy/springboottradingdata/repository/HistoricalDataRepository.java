package de.segoy.springboottradingdata.repository;

import de.segoy.springboottradingdata.model.entity.HistoricalData;

import java.util.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface HistoricalDataRepository extends IBKRDataTypeRepository<HistoricalData> {

List<HistoricalData> findAllByContractId(Integer id);

Optional<HistoricalData>findFirstByContractIdAndTimeAndCount(int id, Timestamp time, int count);

List<HistoricalData> findAllByContractIdAndCreateDateAfter(int id, Date date);
}
