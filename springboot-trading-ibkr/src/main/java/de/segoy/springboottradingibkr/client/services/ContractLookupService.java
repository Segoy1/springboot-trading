package de.segoy.springboottradingibkr.client.services;

import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.EClientSocket;
import de.segoy.springboottradingibkr.client.callback.ContractDetailsCallback;
import de.segoy.springboottradingibkr.client.callback.ContractDetailsCallbackImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class ContractLookupService {

    public List<ContractDetails> lookupContract(Contract contract, Map<Integer, ContractDetailsCallback> callbackMap, Integer id, EClientSocket m_client) throws InterruptedException {
        final CompletableFuture<List<ContractDetails>> future = new CompletableFuture<>();

        synchronized (callbackMap) {
            ContractDetailsCallback callback = new ContractDetailsCallbackImpl(future);
            callbackMap.put(id, callback);
        }
        m_client.reqContractDetails(id, contract);
        try {
            return future.get();
        } catch (final ExecutionException e) {
            return null;
        } finally {
            synchronized (callbackMap) {
                callbackMap.remove(id);
            }
        }
    }
}
