package de.segoy.springboottradingibkr.client.service.contract;

import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.model.OrderData;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContractDataValidator {

    private final UniqueContractDataProvider uniqueContractDataProvider;

    public ContractDataValidator(UniqueContractDataProvider uniqueContractDataProvider) {
        this.uniqueContractDataProvider = uniqueContractDataProvider;
    }

    public boolean validate(OrderData orderData) {

        Optional<ContractData> contractDataOpt = uniqueContractDataProvider.getExistingContractDataOrCallApi(orderData.getContractData());

        if (contractDataOpt.isPresent()) {
            orderData.setContractData(contractDataOpt.get());
            return true;
        }else{
            return false;
        }

    }
}
