package de.segoy.springboottradingdata.repository;

import de.segoy.springboottradingdata.model.data.entity.OptionMarketDataDbo;
import de.segoy.springboottradingdata.model.subtype.Symbol;

public interface OptionMarketDataRepository extends IBKRDataTypeRepository<OptionMarketDataDbo>{

    void deleteBySymbolAndLastTradeDate(Symbol symbol, String lastTradeDate);
}
