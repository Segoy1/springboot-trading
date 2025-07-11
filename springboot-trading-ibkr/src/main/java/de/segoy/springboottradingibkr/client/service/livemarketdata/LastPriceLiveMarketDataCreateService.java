package de.segoy.springboottradingibkr.client.service.livemarketdata;

import com.ib.client.TickType;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.entity.LastPriceLiveMarketDataDbo;
import de.segoy.springboottradingdata.repository.LastPriceLiveMarketDataRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LastPriceLiveMarketDataCreateService {

  private final LastPriceLiveMarketDataRepository lastPriceLiveMarketDataRepository;
  private final TickerIdToContractService tickerIdToContractService;

  @Transactional
  public LastPriceLiveMarketDataDbo createLiveData(int tickerId, double price, TickType tick) {
    LastPriceLiveMarketDataDbo lastPriceLiveMarketDataDbo =
        lastPriceLiveMarketDataRepository
            .findById((long) tickerId)
            .orElseGet(
                () -> {
                  ContractDbo contractDBO = tickerIdToContractService.resolveTickerId(tickerId);
                  return LastPriceLiveMarketDataDbo.builder()
                      .contractDBO(contractDBO)
                      .tickerId((long) tickerId)
                      .build();
                });

    if (tick.equals(TickType.LAST) || tick.equals(TickType.CLOSE)) {
      lastPriceLiveMarketDataDbo.setLastPrice(price);
    } else if (tick.equals(TickType.BID)) {
      lastPriceLiveMarketDataDbo.setBidPrice(price);
    } else if (tick.equals(TickType.ASK)) {
      lastPriceLiveMarketDataDbo.setAskPrice(price);
    }

    return lastPriceLiveMarketDataRepository.save(lastPriceLiveMarketDataDbo);
  }
}
