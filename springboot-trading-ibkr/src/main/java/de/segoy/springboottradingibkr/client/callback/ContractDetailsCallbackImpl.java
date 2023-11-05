package de.segoy.springboottradingibkr.client.callback;

import com.ib.client.ContractDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ContractDetailsCallbackImpl implements ContractDetailsCallback {
    private final List<ContractDetails> list = new ArrayList<>();
    private final CompletableFuture<List<ContractDetails>> future;

    public ContractDetailsCallbackImpl(CompletableFuture<List<ContractDetails>> future) {
        this.future = future;
    }

    @Override
    public void onError(int errorCode, String errorMsg) {
        future.complete(list);
    }

    @Override
    public void onContractDetailsEnd() {
        future.complete(list);
    }

    @Override
    public void onContractDetails(ContractDetails contractDetails) {
        list.add(contractDetails);
    }
}
