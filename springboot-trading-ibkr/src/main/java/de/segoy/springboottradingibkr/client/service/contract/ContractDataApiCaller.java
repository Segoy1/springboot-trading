package de.segoy.springboottradingibkr.client.service.contract;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.model.entity.ContractData;
import de.segoy.springboottradingdata.modelconverter.ContractDataToIBKRContract;
import de.segoy.springboottradingibkr.client.service.ApiCaller;
import org.springframework.stereotype.Service;

/**
 * ContractDataApiCaller
 * Calls IBKR API directly with given Contract Data
 * Package private because it is not meant to be used on its own,
 * for there are no check if contract Data exists already
 */
@Service
class ContractDataApiCaller implements ApiCaller<ContractData> {

    private final ContractDataToIBKRContract contractDataToIBKRContract;
    private final EClientSocket client;

    public ContractDataApiCaller(ContractDataToIBKRContract contractDataToIBKRContract, EClientSocket client) {

        this.contractDataToIBKRContract = contractDataToIBKRContract;
        this.client = client;
    }

    /**
     * Sets Active Api Call Flag for Id and calls the IBKR Api.
     *
     * @param contractData DB object to be converted to ib.Contract to call API
     */
    @Override
    public void callApi(ContractData contractData) {
        client.reqContractDetails(contractData.getId().intValue(), contractDataToIBKRContract.convertContractData(contractData));
    }
}
