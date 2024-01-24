package de.segoy.springboottradingdata.service.apiresponsecheck;

import de.segoy.springboottradingdata.model.data.IBKRDataType;

import java.util.Optional;

public interface OptionalApiResponseChecker<T extends IBKRDataType> {

    public Optional<T> checkForApiResponseAndUpdate(int id);
}
