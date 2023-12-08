package de.segoy.springboottradingdata.repository;

import de.segoy.springboottradingdata.model.HistoricalMarketData;

import java.util.List;

public interface HistoricalMarketDataRepository extends IBKRDataTypeRepository<HistoricalMarketData> {

List<HistoricalMarketData> findAllByGroupId(Integer id);
}
