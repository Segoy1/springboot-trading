package de.segoy.springboottradingibkr.client.service.contract;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.modelconverter.ContractDboToIBKRContract;
import de.segoy.springboottradingibkr.client.service.ApiCaller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * ContractDataApiCaller
 * Calls IBKR API directly with given Contract Data
 * Package private because it is not meant to be used on its own,
 * for there are no check if contract Data exists already
 */
@Service
@RequiredArgsConstructor
class ContractDataApiCaller implements ApiCaller<ContractDbo> {

    private final ContractDboToIBKRContract contractDboToIBKRContract;
    private final EClientSocket client;


    /**
     * Sets Active Api Call Flag for Id and calls the IBKR Api.
     *
     * @param contractDBO DB object to be converted to ib.Contract to call API
     */
    @Override
    public void callApi(ContractDbo contractDBO) {
        client.reqContractDetails(
                contractDBO.getId().intValue(), contractDboToIBKRContract.convertContractData(contractDBO));
    }
}
