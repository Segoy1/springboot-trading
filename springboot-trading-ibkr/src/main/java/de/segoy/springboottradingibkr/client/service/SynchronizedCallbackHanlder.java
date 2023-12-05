package de.segoy.springboottradingibkr.client.service;

import com.ib.client.ContractDetails;
import de.segoy.springboottradingibkr.client.callback.ContractDetailsCallback;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Service
public class SynchronizedCallbackHanlder {

    public synchronized void contractDetails(int reqId, ContractDetails contractDetails,  Map<Integer, ContractDetailsCallback> m_callbackMap) {
        ContractDetailsCallback callback = m_callbackMap.get(reqId);
        if (callback != null) {
            callback.onContractDetails(contractDetails);
        }
    }

    public synchronized void contractDetailsEnd(int reqId, Map<Integer, ContractDetailsCallback> m_callbackMap){
        ContractDetailsCallback callback = m_callbackMap.get(reqId);

        if (callback != null) {
            callback.onContractDetailsEnd();
        }
    }

    public synchronized void contractDetailsError(int id, int errorCode, String errorMsg, Map<Integer, ContractDetailsCallback> m_callbackMap){
        final ContractDetailsCallback callback = m_callbackMap.get(id);
        if (callback != null) {
            callback.onError(errorCode, errorMsg);
        } else if (id == -1) {
            final Collection<ContractDetailsCallback> callbacks = new ArrayList<>(m_callbackMap.size());
                callbacks.addAll(m_callbackMap.values());
            for (final ContractDetailsCallback cb : callbacks) {
                cb.onError(errorCode, errorMsg);
            }
        }
    }
}
