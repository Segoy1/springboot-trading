package de.segoy.springboottradingibkr.client.service.contract;

import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UniqueContractDataProvider {
    private final ContractDataRepository contractDataRepository;
    private final ContractDataCallAndResponseHandler contractDataCallAndResponseHandler;

    public UniqueContractDataProvider(ContractDataRepository contractDataRepository, ContractDataCallAndResponseHandler contractDataCallAndResponseHandler) {
        this.contractDataRepository = contractDataRepository;
        this.contractDataCallAndResponseHandler = contractDataCallAndResponseHandler;
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
        Optional<ContractData> contractOpt = contractDataRepository.findFirstBySymbolAndSecurityTypeAndExchange(contractData.getSymbol(),
                contractData.getSecurityType(),
                contractData.getExchange());
        return contractOpt.isPresent()?contractOpt: contractDataCallAndResponseHandler.callContractDetailsFromAPI(contractData);
    }

    private Optional<ContractData> getIndexData(ContractData contractData) {
        Optional<ContractData> contractOpt = contractDataRepository.findFirstBySymbolAndSecurityTypeAndExchange(contractData.getSymbol(),
                contractData.getSecurityType(),
                contractData.getExchange());
        return contractOpt.isPresent()?contractOpt: contractDataCallAndResponseHandler.callContractDetailsFromAPI(contractData);
    }

    private Optional<ContractData> getOptionContractData(ContractData contractData) {
        Optional<ContractData> contractOpt = contractDataRepository.findFirstByLastTradeDateAndSymbolAndStrikeAndRight(contractData.getLastTradeDate(),
                contractData.getSymbol(),
                contractData.getStrike(),
                contractData.getRight());
        return contractOpt.isPresent() ? contractOpt : contractDataCallAndResponseHandler.callContractDetailsFromAPI(contractData);
    }
}
