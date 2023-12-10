package de.segoy.springboottradingibkr.client.service.contract;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.modelconverter.ContractDataToIBKRContract;
import org.springframework.stereotype.Service;

/**
 * ContractDataApiCaller
 * Calls IBKR API directly with given Contract Data
 * Package private because it is not meant to be used on its own,
 * for there are no check if contract Data exists already
 */
@Service
class ContractDataApiCaller {

    private final ContractDataToIBKRContract contractDataToIBKRContract;
    private final EClientSocket client;

    public ContractDataApiCaller(ContractDataToIBKRContract contractDataToIBKRContract, EClientSocket client) {

        this.contractDataToIBKRContract = contractDataToIBKRContract;
        this.client = client;
    }

    /**
     * Sets Active Api Call Flag for Id and calls the IBKR Api.
     *
     * @param nextId needs to be generated and parsed so DB Object can be identified
     * @param contractData DB object to be converted to ib.Contract to call API
     */
    public void callApi(Long nextId, ContractData contractData) {
        Contract ibkrContract = contractDataToIBKRContract.convertContractData(contractData);
        client.reqContractDetails(nextId.intValue(), ibkrContract);
    }
}
