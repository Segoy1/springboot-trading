package de.segoy.springboottradingdata.optionstradingservice;

import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.service.StrategyNameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutoTradeIdService {

  private final AutotradeDbAndTickerIdEncoder autotradeDbAndTickerIdEncoder;
  private final StrategyNameService strategyNameService;

  /** if LastTradeDate is today and Symbol is SPX it is Auto Trade */
  public Long setIdForAutoTrade(ContractDbo contractDbo) {
    if (contractDbo.getLastTradeDate() != null && !contractDbo.getComboLegs().isEmpty()) {

      return autotradeDbAndTickerIdEncoder.generateLongForLastTradeDateBySymbolAndStrategy(
          Long.valueOf(contractDbo.getLastTradeDate()),
          contractDbo.getSymbol(),
          strategyNameService.resolveStrategyFromComboLegs(contractDbo.getComboLegs()));
    }
    return null;
  }
}
