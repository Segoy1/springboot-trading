package de.segoy.springboottradingibkr.client.service.tradinghours;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.entity.TradingHoursDbo;
import de.segoy.springboottradingdata.optionstradingservice.LastTradeDateBuilder;
import de.segoy.springboottradingdata.repository.TradingHoursRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;
import de.segoy.springboottradingibkr.client.service.contract.ContractDataCallAndResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TradingHoursService {

  private final TradingHoursRepository tradingHoursRepository;
  private final ContractDataCallAndResponseHandler contractDataCallAndResponseHandler;
  private final RepositoryRefreshService repositoryRefreshService;
  private final EClientSocket eClientSocket;
  private final LastTradeDateBuilder lastTradeDateBuilder;

  public boolean isOpenToday(ContractDbo contractDbo) {
    return getTradingHourString(contractDbo).contains(lastTradeDateBuilder.getDateStringFromToday());
  }

  public String getTradingHourString(ContractDbo contractDbo) {
    return getTradingHoursFromDb(contractDbo).getTradingHours();
  }

  /**
   * Recursive Call might result in an infinite Loop if no ContractDetails are answered
   *
   * @param contractDbo
   * @return
   */
  private TradingHoursDbo getTradingHoursFromDb(ContractDbo contractDbo) {

    return tradingHoursRepository
        .findBySymbol(contractDbo.getSymbol())
        .orElseGet(
            () -> {
              contractDataCallAndResponseHandler.callContractDetailsFromAPI(contractDbo);
              repositoryRefreshService.clearCacheAndWait(tradingHoursRepository);
              return getTradingHoursFromDb(contractDbo);
            });
  }
}
