package de.segoy.springboottradingibkr.client.callback;

import com.ib.client.ContractDetails;

public interface ContractDetailsCallback {
    void onContractDetails(ContractDetails contractDetails);
    void onContractDetailsEnd();
    void onError(int errorCode, String errorMsg);
}
