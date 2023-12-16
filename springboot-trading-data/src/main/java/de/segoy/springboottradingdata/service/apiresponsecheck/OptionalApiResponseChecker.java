package de.segoy.springboottradingdata.service.apiresponsecheck;

import de.segoy.springboottradingdata.model.entity.IBKRDataTypeEntity;

import java.util.Optional;

public interface OptionalApiResponseChecker<T extends IBKRDataTypeEntity> {

    public Optional<T> checkForApiResponseAndUpdate(int id);
}
