package de.segoy.springboottradingdata.service.apiresponsecheck;

import de.segoy.springboottradingdata.model.IBKRDataTypeEntity;

import java.util.List;

public interface ApiResponseCheckerForList<T extends IBKRDataTypeEntity> {

    public List<T> checkForApiResponseAndUpdate(int id);
}
