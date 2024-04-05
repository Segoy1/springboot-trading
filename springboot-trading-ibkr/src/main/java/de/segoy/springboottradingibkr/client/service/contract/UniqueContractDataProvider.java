package de.segoy.springboottradingibkr.client.service.contract;

import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import de.segoy.springboottradingdata.service.ComboContractDataFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.OptionalLong;

@Service
@RequiredArgsConstructor
public class UniqueContractDataProvider {

    private final ContractDataRepository contractDataRepository;
    private final ContractDataCallAndResponseHandler contractDataCallAndResponseHandler;
    private final ComboContractDataFinder comboContractDataFinder;


    public Optional<ContractData> getExistingContractDataOrCallApi(ContractData contractData) {
        //TODO extend for new Types that need to be used
        return switch (contractData.getSecurityType()) {
            case OPT -> getOptionContractData(contractData);
            case STK, IND -> getIndexOrStockData(contractData);
            case BAG -> getComboLegOptionData(contractData);
            default -> Optional.empty();
        };
    }

    private Optional<ContractData> getComboLegOptionData(ContractData contractData) {
        if (contractData.getId() != null
                && contractDataRepository.findById(contractData.getId()).isPresent()) {
            return contractDataRepository.findById(contractData.getId());
        }
        OptionalLong id = comboContractDataFinder.checkContractWithComboLegs(contractData.getComboLegs());
        if (id.isPresent()) {
            return contractDataRepository.findById(id.getAsLong());
        }
        return Optional.of(contractDataRepository.save(contractData));
    }

    private Optional<ContractData> getIndexOrStockData(ContractData contractData) {
        Optional<ContractData> contractOpt =
                contractDataRepository.findFirstBySymbolAndSecurityTypeAndCurrency(contractData.getSymbol(),
                        contractData.getSecurityType(),
                        contractData.getCurrency());
        return contractOpt.isPresent() ? contractOpt : contractDataCallAndResponseHandler.callContractDetailsFromAPI(
                contractData);
    }

    private Optional<ContractData> getOptionContractData(ContractData contractData) {
        Optional<ContractData> contractOpt = contractDataRepository.findFirstByLastTradeDateAndSymbolAndStrikeAndRight(
                contractData.getLastTradeDate(),
                contractData.getSymbol(),
                contractData.getStrike(),
                contractData.getRight());
        return contractOpt.isPresent() ? contractOpt : contractDataCallAndResponseHandler.callContractDetailsFromAPI(
                contractData);
    }
}
