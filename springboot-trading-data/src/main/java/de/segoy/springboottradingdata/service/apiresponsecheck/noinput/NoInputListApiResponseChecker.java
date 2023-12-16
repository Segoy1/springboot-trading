package de.segoy.springboottradingdata.service.apiresponsecheck.noinput;

import de.segoy.springboottradingdata.model.entity.IBKRDataTypeEntity;

import java.util.List;

public interface NoInputListApiResponseChecker<T extends IBKRDataTypeEntity> {

    List<T> checkForApiResponseAndUpdate();
}
