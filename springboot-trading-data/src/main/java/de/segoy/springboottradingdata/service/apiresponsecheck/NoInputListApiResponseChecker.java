package de.segoy.springboottradingdata.service.apiresponsecheck;

import de.segoy.springboottradingdata.model.IBKRDataTypeEntity;
import de.segoy.springboottradingdata.model.PositionData;

import java.util.List;

public interface NoInputListApiResponseChecker<T extends IBKRDataTypeEntity> {

    List<T> checkForApiResponseAndUpdate();
}
