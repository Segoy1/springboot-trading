package de.segoy.springboottradingibkr.client.service;

import de.segoy.springboottradingdata.model.data.IBKRDataType;

import java.util.Optional;

public interface OptionalApiResponseChecker<T extends IBKRDataType> {

    public Optional<T> checkForApiResponseAndUpdate(int id);
}
