package de.segoy.springboottradingibkr.client.service.livemarketdata;

import com.ib.client.Types;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.dataobject.ContractDataTemplates;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.entity.LastPriceLiveMarketDataDbo;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import de.segoy.springboottradingdata.repository.ContractRepository;
import de.segoy.springboottradingdata.repository.LastPriceLiveMarketDataRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class LastPriceLiveMarketDataCreateService {

  private final PropertiesConfig propertiesConfig;
  private final LastPriceLiveMarketDataRepository lastPriceLiveMarketDataRepository;
  private final ContractRepository contractRepository;

  @Transactional
  public LastPriceLiveMarketDataDbo createLiveData(int tickerId, double price) {
    ContractDbo contractDBO;
      contractDBO =
          contractRepository
              .findFirstBySymbolAndSecurityTypeAndCurrency(Symbol.SPX, Types.SecType.IND, "USD")
              .orElseGet(() -> contractRepository.save(ContractDataTemplates.SpxOptionData()));
    LastPriceLiveMarketDataDbo lastPriceLiveMarketDataDbo =
        LastPriceLiveMarketDataDbo.builder()
            .tickerId((long) tickerId)
            .lastPrice(price)
            .contractDBO(contractDBO)
            .createDate(new Date(Instant.now().toEpochMilli()))
            .build();
    return lastPriceLiveMarketDataRepository.save(lastPriceLiveMarketDataDbo);
  }
}
