package de.segoy.springboottradingibkr.client.service.contract;

import de.segoy.springboottradingdata.model.entity.ContractData;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import de.segoy.springboottradingdata.service.ComboContractDataFinder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.OptionalLong;

@Service
public class UniqueContractDataProvider {
    private final ContractDataRepository contractDataRepository;
    private final ContractDataCallAndResponseHandler contractDataCallAndResponseHandler;
    private final ComboContractDataFinder comboContractDataFinder;

    public UniqueContractDataProvider(ContractDataRepository contractDataRepository,
                                      ContractDataCallAndResponseHandler contractDataCallAndResponseHandler,
                                      ComboContractDataFinder comboContractDataFinder) {
        this.contractDataRepository = contractDataRepository;
        this.contractDataCallAndResponseHandler = contractDataCallAndResponseHandler;
        this.comboContractDataFinder = comboContractDataFinder;
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
        OptionalLong id = comboContractDataFinder.checkContractWithComboLegs(contractData.getComboLegs());
        if (id.isPresent()) {
            return contractDataRepository.findById(id.getAsLong());
        }
        return Optional.of(contractDataRepository.save(contractData));
    }

    private Optional<ContractData> getStockData(ContractData contractData) {
        Optional<ContractData> contractOpt =
                contractDataRepository.findFirstBySymbolAndSecurityTypeAndCurrency(contractData.getSymbol(),
                        contractData.getSecurityType(),
                        contractData.getCurrency());
        return contractOpt.isPresent() ? contractOpt : contractDataCallAndResponseHandler.callContractDetailsFromAPI(
                contractData);
    }

    private Optional<ContractData> getIndexData(ContractData contractData) {
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
