package de.segoy.springboottradingibkr.client.service.contract;

import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import de.segoy.springboottradingdata.service.ComboContractDataFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.OptionalLong;

@Service
@RequiredArgsConstructor
public class UniqueContractDataProvider {

    private final ContractDataRepository contractDataRepository;
    private final ContractDataCallAndResponseHandler contractDataCallAndResponseHandler;
    private final ComboContractDataFinder comboContractDataFinder;


    @Transactional
    public Optional<ContractDataDBO> getExistingContractDataOrCallApi(ContractDataDBO contractDataDBO) {
        //TODO extend for new Types that need to be used
        return switch (contractDataDBO.getSecurityType()) {
            case OPT -> getOptionContractData(contractDataDBO);
            case STK, IND -> getIndexOrStockData(contractDataDBO);
            case BAG -> getComboLegOptionData(contractDataDBO);
            default -> Optional.empty();
        };
    }

    private Optional<ContractDataDBO> getComboLegOptionData(ContractDataDBO contractDataDBO) {
        if (contractDataDBO.getId() != null
                && contractDataRepository.findById(contractDataDBO.getId()).isPresent()) {
            return contractDataRepository.findById(contractDataDBO.getId());
        }
        OptionalLong id = comboContractDataFinder.checkContractWithComboLegs(contractDataDBO.getComboLegs());
        if (id.isPresent()) {
            return contractDataRepository.findById(id.getAsLong());
        }
        return Optional.of(contractDataRepository.save(contractDataDBO));
    }

    private Optional<ContractDataDBO> getIndexOrStockData(ContractDataDBO contractDataDBO) {
        Optional<ContractDataDBO> contractOpt =
                contractDataRepository.findFirstBySymbolAndSecurityTypeAndCurrency(contractDataDBO.getSymbol(),
                        contractDataDBO.getSecurityType(),
                        contractDataDBO.getCurrency());
        return contractOpt.isPresent() ? contractOpt : contractDataCallAndResponseHandler.callContractDetailsFromAPI(
                contractDataDBO);
    }

    private Optional<ContractDataDBO> getOptionContractData(ContractDataDBO contractDataDBO) {
        Optional<ContractDataDBO> contractOpt = contractDataRepository.findFirstByLastTradeDateAndSymbolAndStrikeAndRight(
                contractDataDBO.getLastTradeDate(),
                contractDataDBO.getSymbol(),
                contractDataDBO.getStrike(),
                contractDataDBO.getRight());
        return contractOpt.isPresent() ? contractOpt : contractDataCallAndResponseHandler.callContractDetailsFromAPI(
                contractDataDBO);
    }
}
