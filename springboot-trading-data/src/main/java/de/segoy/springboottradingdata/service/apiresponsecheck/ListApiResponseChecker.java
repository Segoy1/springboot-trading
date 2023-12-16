package de.segoy.springboottradingdata.service.apiresponsecheck;

import de.segoy.springboottradingdata.model.entity.IBKRDataTypeEntity;

import java.util.List;

public interface ListApiResponseChecker<T extends IBKRDataTypeEntity> {

    public List<T> checkForApiResponseAndUpdate(int id);
}
