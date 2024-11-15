package de.segoy.springboottradingibkr.client.service.contract;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.repository.ContractRepository;
import de.segoy.springboottradingdata.service.ComboContractDataFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.OptionalLong;

@Service
@RequiredArgsConstructor
public class UniqueContractDataProvider {

  private final ContractRepository contractRepository;
  private final ContractDataCallAndResponseHandler contractDataCallAndResponseHandler;
  private final ComboContractDataFinder comboContractDataFinder;

  @Transactional
  public Optional<ContractDbo> getExistingContractDataOrCallApi(ContractDbo contractDBO) {
    // TODO extend for new Types that need to be used
    return switch (contractDBO.getSecurityType()) {
      case OPT -> getOptionContractData(contractDBO);
      case STK, IND -> getIndexOrStockData(contractDBO);
      case BAG -> getComboLegOptionData(contractDBO);
      default -> Optional.empty();
    };
  }

  private Optional<ContractDbo> getComboLegOptionData(ContractDbo contractDBO) {
    if (contractDBO.getId() != null
        && contractRepository.findById(contractDBO.getId()).isPresent()) {
      return contractRepository.findById(contractDBO.getId());
    }
    OptionalLong id =
        comboContractDataFinder.checkContractWithComboLegs(contractDBO.getComboLegs());
    if (id.isPresent()) {
      return contractRepository.findById(id.getAsLong());
    } else {
      contractDBO
          .getComboLegs()
          .forEach(
              leg ->
                  getOptionContractData(
                      ContractDbo.builder()
                          .contractId(leg.getContractId())
                          .securityType(Types.SecType.OPT)
                          .currency(contractDBO.getCurrency())
                          .exchange(contractDBO.getExchange())
                          .symbol(contractDBO.getSymbol())
                          .build()));
      return Optional.of(contractRepository.save(contractDBO));
    }
  }

  private Optional<ContractDbo> getIndexOrStockData(ContractDbo contractDBO) {
    Optional<ContractDbo> contractOpt =
        contractRepository.findFirstBySymbolAndSecurityTypeAndCurrency(
            contractDBO.getSymbol(), contractDBO.getSecurityType(), contractDBO.getCurrency());
    return contractOpt.isPresent()
        ? contractOpt
        : contractDataCallAndResponseHandler.callContractDetailsFromAPI(contractDBO);
  }

  private Optional<ContractDbo> getOptionContractData(ContractDbo contractDBO) {
    Optional<ContractDbo> contractOpt =
        contractRepository.findFirstByLastTradeDateAndSymbolAndStrikeAndRight(
            contractDBO.getLastTradeDate(),
            contractDBO.getSymbol(),
            contractDBO.getStrike(),
            contractDBO.getRight());
    return contractOpt.isPresent()
        ? contractOpt
        : contractDataCallAndResponseHandler.callContractDetailsFromAPI(contractDBO);
  }
}
