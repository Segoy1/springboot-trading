package de.segoy.springboottradingibkr.client.services;

import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.repository.ComboLegDataRepository;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UniqueContractDataProvider {
    private final ContractDataRepository contractDataRepository;
    private final ContractDataApiCaller contractDataApiCaller;
    private final ComboLegDataRepository comboLegDataRepository;

    public UniqueContractDataProvider(ContractDataRepository contractDataRepository, ContractDataApiCaller contractDataApiCaller, ComboLegDataRepository comboLegDataRepository) {
        this.contractDataRepository = contractDataRepository;
        this.contractDataApiCaller = contractDataApiCaller;
        this.comboLegDataRepository = comboLegDataRepository;
    }

    public Optional<ContractData> getExistingContractDataOrCallApi(ContractData contractData) {
        //TODO extend for new Types that need to be used
        return switch (contractData.getSecurityType()) {
            case OPT -> getOptionContractData(contractData);
            case STK -> getStockOptionData(contractData);
            case BAG -> getComboLegOptionData(contractData);
            default -> Optional.empty();
        };
    }

    private Optional<ContractData> getComboLegOptionData(ContractData contractData) {
        return Optional.of(contractDataRepository.save(contractData));
    }

    private Optional<ContractData> getStockOptionData(ContractData contractData) {
        //TODO implementation for Stock
        return contractDataApiCaller.callContractDetailsFromAPI(contractData);
    }

    private Optional<ContractData> getOptionContractData(ContractData contractData) {
        Optional<ContractData> contractOpt = contractDataRepository.findFirstByLastTradeDateAndSymbolAndStrikeAndRight(contractData.getLastTradeDate(),
                contractData.getSymbol(),
                contractData.getStrike(),
                contractData.getRight());
        return contractOpt.isPresent() ? contractOpt : contractDataApiCaller.callContractDetailsFromAPI(contractData);
    }
}
