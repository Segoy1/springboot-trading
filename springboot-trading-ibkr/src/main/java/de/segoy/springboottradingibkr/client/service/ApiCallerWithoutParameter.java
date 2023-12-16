package de.segoy.springboottradingibkr.client.service;

import de.segoy.springboottradingdata.model.entity.IBKRDataTypeEntity;

public interface ApiCallerWithoutParameter<T extends IBKRDataTypeEntity> {

    void callApi();
}
