package de.segoy.springboottradingdata.service;

import de.segoy.springboottradingdata.model.data.message.ErrorMessage;
import org.springframework.stereotype.Component;

@Component
public class ErrorCodeMapper {

    //TODO get full list of error messages and handle them.
    public void mapError(ErrorMessage errorMessage) {
        switch (errorMessage.getErrorCode()) {
            case 399:
                break;
            case 10311:
                break;
            case 10185:
                //not Subscribed to Account PNL, ignore
                break;
            case 10186:
                //not Subscribed to Single PNL, ignore
                break;
            case 2150:
                //Todo find out what this is
                break;
            default:
                throw new RuntimeException("Error occured: " + errorMessage.getMessage());

        }
    }
}
