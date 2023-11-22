package de.segoy.springboottradingibkr.client.services;

import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import org.springframework.stereotype.Service;

@Service
public class UniqueContractDataProvider {
    private final ContractDataRepository contractDataRepository;
    private final ContractDataApiCaller contractDataApiCaller;

    public UniqueContractDataProvider(ContractDataRepository contractDataRepository, ContractDataApiCaller contractDataApiCaller) {
        this.contractDataRepository = contractDataRepository;
        this.contractDataApiCaller = contractDataApiCaller;
    }

    public ContractData getExistingContractDataOrCallApi(ContractData contractData) {
        return switch (contractData.getSecurityType()) {
            case OPT -> getOptionContractData(contractData);
            case STK -> getStockOptionData(contractData);
            case BAG -> getComboLegOptionData(contractData);
            default -> null;
        };
    }

    private ContractData getComboLegOptionData(ContractData contractData) {
        //TODO add Method in repo to check if exists
        return contractDataApiCaller.callContractDetailsFromAPI(contractDataRepository.save(contractData));
    }

    private ContractData getStockOptionData(ContractData contractData) {
        //TODO implementation for Stock
        return contractData;
    }

    private ContractData getOptionContractData(ContractData contractData) {
        return contractDataRepository.findFirstByLastTradeDateAndSymbolAndStrikeAndRight(contractData.getLastTradeDate(),
                contractData.getSymbol(),
                contractData.getStrike(),
                contractData.getRight()).orElseGet(() -> contractDataApiCaller.callContractDetailsFromAPI(contractDataRepository.save(contractData)));
    }
}
