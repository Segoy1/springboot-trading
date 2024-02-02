package de.segoy.springboottradingibkr.client.service.position;

import de.segoy.springboottradingibkr.client.service.ApiCallerWithoutParameter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final @Qualifier("PoistionApiCaller")ApiCallerWithoutParameter positionApiCaller;

    public void callPortfolio() {
        positionApiCaller.callApi();
    }
}
