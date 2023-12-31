package de.segoy.springboottradingibkr.client.service.position;

import de.segoy.springboottradingdata.model.entity.PositionData;
import de.segoy.springboottradingdata.service.apiresponsecheck.noinput.PositionApiResponseChecker;
import de.segoy.springboottradingibkr.client.service.ApiCallerWithoutParameter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionService {

    private final ApiCallerWithoutParameter<PositionData> positionApiCaller;
    private final PositionApiResponseChecker positionDataApiResponseChecker;
    private final PortfolioUpdateService portfolioUpdateService;

    public PositionService(ApiCallerWithoutParameter<PositionData> positionApiCaller,
                           PositionApiResponseChecker positionDataApiResponseChecker,
                           PortfolioUpdateService portfolioUpdateService) {
        this.positionApiCaller = positionApiCaller;
        this.positionDataApiResponseChecker = positionDataApiResponseChecker;
        this.portfolioUpdateService = portfolioUpdateService;
    }

    public List<PositionData> getUpdatedPortfolio() {
        positionApiCaller.callApi();
        List<PositionData> positions = positionDataApiResponseChecker.checkForApiResponseAndUpdate();
        return portfolioUpdateService.updatePortfolio(positions);
    }
}
