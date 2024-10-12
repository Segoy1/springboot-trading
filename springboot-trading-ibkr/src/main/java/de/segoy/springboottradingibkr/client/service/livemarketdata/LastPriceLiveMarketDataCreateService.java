package de.segoy.springboottradingibkr.client.service.livemarketdata;

import com.ib.client.Types;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.dataobject.ContractDataTemplates;
import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.model.data.entity.LastPriceLiveMarketDataDBO;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
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
  private final ContractDataRepository contractDataRepository;

  @Transactional
  public LastPriceLiveMarketDataDBO createLiveData(int tickerId, double price) {
    ContractDataDBO contractDataDBO;
      contractDataDBO =
          contractDataRepository
              .findFirstBySymbolAndSecurityTypeAndCurrency(Symbol.SPX, Types.SecType.IND, "USD")
              .orElseGet(() -> contractDataRepository.save(ContractDataTemplates.SpxOptionData()));
    LastPriceLiveMarketDataDBO lastPriceLiveMarketDataDBO =
        LastPriceLiveMarketDataDBO.builder()
            .tickerId((long) tickerId)
            .lastPrice(price)
            .contractDataDBO(contractDataDBO)
            .createDate(new Date(Instant.now().toEpochMilli()))
            .build();
    return lastPriceLiveMarketDataRepository.save(lastPriceLiveMarketDataDBO);
  }
}
