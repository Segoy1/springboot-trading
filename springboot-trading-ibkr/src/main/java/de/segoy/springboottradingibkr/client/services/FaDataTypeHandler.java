package de.segoy.springboottradingibkr.client.services;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.model.FinancialAdvisor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FaDataTypeHandler {

    private final EClientSocket m_client;

    public FaDataTypeHandler(EClientSocket m_client) {
        this.m_client = m_client;
    }

    public void handleFaDataType(int faDataType, String xml, Map<Integer, String> faMap, boolean faError) {

        if (faDataType == EClientSocket.GROUPS || faDataType == EClientSocket.PROFILES || faDataType == EClientSocket.ALIASES) {
            faMap.put(faDataType, xml);
        } else {
            return;
        }

        if (!faError &&
                !(faMap.get(EClientSocket.GROUPS) == null || faMap.get(EClientSocket.PROFILES) == null || faMap.get(EClientSocket.ALIASES) == null)) {
            FinancialAdvisor model = new FinancialAdvisor();
            model.receiveInitialXML(faMap.get(EClientSocket.GROUPS), faMap.get(EClientSocket.PROFILES), faMap.get(EClientSocket.ALIASES));
            model.setVisible(true);

            if (!model.isM_rc()) {
                return;
            }

            m_client.replaceFA(0, EClientSocket.GROUPS, model.getGroupsXML());
            m_client.replaceFA(1, EClientSocket.PROFILES, model.getProfilesXML());
            m_client.replaceFA(2, EClientSocket.ALIASES, model.getAliasesXML());

        }
    }
}
