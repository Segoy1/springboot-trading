package de.segoy.springboottradingweb;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.dataobject.ContractDataTemplates;
import de.segoy.springboottradingibkr.client.service.contract.ContractDataCallAndResponseHandler;
import de.segoy.springboottradingibkr.client.service.position.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationStartupProcedureService {


    private final PropertiesConfig propertiesConfig;
    private final  ConnectionInitiator connection;
    private final PositionService positionService;
    private final ContractDataCallAndResponseHandler contractDataCallAndResponseHandler;

    public void onStartUp(){

        connection.connect(propertiesConfig.getTradingPort());
        positionService.callPortfolio();
        contractDataCallAndResponseHandler.callContractDetailsFromAPI(ContractDataTemplates.SpxData());
    }

}
