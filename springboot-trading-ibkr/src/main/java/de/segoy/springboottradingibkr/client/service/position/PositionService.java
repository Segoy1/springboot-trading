package de.segoy.springboottradingibkr.client.service.position;

import de.segoy.springboottradingibkr.client.service.ApiCallerWithoutParameter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class PositionService {

    private final ApiCallerWithoutParameter positionApiCaller;

    public PositionService(@Qualifier("PoistionApiCaller") ApiCallerWithoutParameter positionApiCaller) {
        this.positionApiCaller = positionApiCaller;
    }

    public void callPortfolio() {
        positionApiCaller.callApi();
    }
}
