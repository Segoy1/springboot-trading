package de.segoy.springboottradingibkr.client.service.contract;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.modelconverter.ContractDataToIBKRContract;
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
class ContractDataApiCaller implements ApiCaller<ContractDataDBO> {

    private final ContractDataToIBKRContract contractDataToIBKRContract;
    private final EClientSocket client;


    /**
     * Sets Active Api Call Flag for Id and calls the IBKR Api.
     *
     * @param contractDataDBO DB object to be converted to ib.Contract to call API
     */
    @Override
    public void callApi(ContractDataDBO contractDataDBO) {
        client.reqContractDetails(
                contractDataDBO.getId().intValue(), contractDataToIBKRContract.convertContractData(contractDataDBO));
    }
}
