package de.segoy.springboottradingibkr.client.service;

import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UniqueContractDataProvider {
    private final ContractDataRepository contractDataRepository;
    private final ContractDataApiCaller contractDataApiCaller;

    public UniqueContractDataProvider(ContractDataRepository contractDataRepository, ContractDataApiCaller contractDataApiCaller) {
        this.contractDataRepository = contractDataRepository;
        this.contractDataApiCaller = contractDataApiCaller;
    }

    public Optional<ContractData> getExistingContractDataOrCallApi(ContractData contractData) {
        //TODO extend for new Types that need to be used
        return switch (contractData.getSecurityType()) {
            case OPT -> getOptionContractData(contractData);
            case STK -> getStockData(contractData);
            case BAG -> getComboLegOptionData(contractData);
            case IND -> getIndexData(contractData);
            default -> Optional.empty();
        };
    }

    private Optional<ContractData> getComboLegOptionData(ContractData contractData) {
        if (contractData.getId() != null
                && contractDataRepository.findById(contractData.getId()).isPresent()) {
            return contractDataRepository.findById(contractData.getId());
        }
        return Optional.of(contractDataRepository.save(contractData));
    }

    private Optional<ContractData> getStockData(ContractData contractData) {
        //TODO implementation for Stock
        return contractDataApiCaller.callContractDetailsFromAPI(contractData);
    }
    private  Optional<ContractData> getIndexData(ContractData contractData) {
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
