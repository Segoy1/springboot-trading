package de.segoy.springboottradingdata.repository;

import de.segoy.springboottradingdata.model.HistoricalData;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface HistoricalDataRepository extends IBKRDataTypeRepository<HistoricalData> {

List<HistoricalData> findAllByContractId(Integer id);

Optional<HistoricalData>findFirstByContractIdAndTime(Integer id, Timestamp time);
}
