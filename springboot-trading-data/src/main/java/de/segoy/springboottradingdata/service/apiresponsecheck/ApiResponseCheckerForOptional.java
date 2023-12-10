package de.segoy.springboottradingdata.service.apiresponsecheck;

import de.segoy.springboottradingdata.model.IBKRDataTypeEntity;

import java.util.Optional;

public interface ApiResponseCheckerForOptional <T extends IBKRDataTypeEntity> {

    public Optional<T> checkForApiResponseAndUpdate(int id);
}
