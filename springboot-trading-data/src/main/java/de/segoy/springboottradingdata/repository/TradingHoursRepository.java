package de.segoy.springboottradingdata.repository;

import de.segoy.springboottradingdata.model.data.entity.TradingHoursDbo;
import de.segoy.springboottradingdata.model.subtype.Symbol;

import java.util.Optional;

public interface TradingHoursRepository extends IBKRDataTypeRepository<TradingHoursDbo> {

  Optional<TradingHoursDbo> findBySymbol(Symbol symbol);
    }
