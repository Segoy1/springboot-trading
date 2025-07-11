package de.segoy.springboottradingdata.repository;

import de.segoy.springboottradingdata.model.data.entity.OptionChainDbo;
import de.segoy.springboottradingdata.model.subtype.Symbol;

public interface OptionChainRepository extends IBKRDataTypeRepository<OptionChainDbo>{

    void deleteBySymbolAndLastTradeDate(Symbol symbol, Long lastTradeDate);
}
