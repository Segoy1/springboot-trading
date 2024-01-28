package de.segoy.springboottradingdata.service;

import de.segoy.springboottradingdata.model.data.message.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class ErrorCodeMapper {

    public Optional<ErrorMessage> mapError(ErrorMessage errorMessage) {
        switch (errorMessage.getErrorCode()) {
            case 322:
                //Duplicate Ticker ID for Market Data will be handled in Frontend
                return Optional.of(errorMessage);
            case 502:
                log.warn(errorMessage.getMessage());
                break;
            case 399:
                log.warn("399:"+ errorMessage.getMessage());
                break;
            case 10311:
                //Order will be directly Routed, no auto submit
                log.warn("10311:"+ errorMessage.getMessage());
                return Optional.of(errorMessage);
            case 10185:
                //not Subscribed to Account PNL, ignore
                break;
            case 10186:
                //not Subscribed to Single PNL, ignore
                break;
            case 2104, 2106, 2108, 2158:
                //Messages on startup just stating Connection is OK.
                log.info(errorMessage.getMessage());
                break;
            case 2150:
                //Invalid Position derived from strategies: Ignore
                break;
            default:
                log.warn("Error: "+ errorMessage.getMessageId()+", Code: "+errorMessage.getErrorCode()+", message: "+ errorMessage.getMessage());
                return Optional.of(errorMessage);
        }
        return Optional.empty();
    }
}
