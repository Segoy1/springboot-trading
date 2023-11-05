package de.segoy.springboottradingibkr.client.services;

import de.segoy.springboottradingibkr.client.FaErrorCode;
import de.segoy.springboottradingibkr.model.MktDepthModel;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ErrorCodeHandler {

    public boolean isFaError(int errorCode) {
        boolean faError = false;
        if (errorCode == FaErrorCode.MKT_DEPTH_DATA_RESET.getCode()) {
            return faError;
        }
        for (FaErrorCode faErrorCode : FaErrorCode.values()) {
            faError |= (errorCode == faErrorCode.getCode());
        }
        return faError;
    }

    public void handleDataReset(int id, int errorCode, Map<Integer, MktDepthModel> m_mapRequestToMktDepthModel){
        if (errorCode == FaErrorCode.MKT_DEPTH_DATA_RESET.getCode()) {

            MktDepthModel depthDialog = m_mapRequestToMktDepthModel.get(id);
            if ( depthDialog != null ) {
                depthDialog.reset();
            } else {
                System.err.println("cannot find dialog that corresponds to request id ["+id+"]");
            }
        }

    }
}
