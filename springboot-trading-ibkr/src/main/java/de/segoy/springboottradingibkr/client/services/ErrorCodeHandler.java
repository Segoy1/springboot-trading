package de.segoy.springboottradingibkr.client.services;

import de.segoy.springboottradingibkr.client.FaErrorCode;
import de.segoy.springboottradingdata.model.MktDepth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
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

    public void handleDataReset(int id, int errorCode, Map<Integer, MktDepth> m_mapRequestToMktDepthModel){
        if (errorCode == FaErrorCode.MKT_DEPTH_DATA_RESET.getCode()) {

            MktDepth depthDialog = m_mapRequestToMktDepthModel.get(id);
            if ( depthDialog != null ) {
                depthDialog.reset();
            } else {
                log.error("cannot find dialog that corresponds to request id ["+id+"]");
            }
        }

    }
}
