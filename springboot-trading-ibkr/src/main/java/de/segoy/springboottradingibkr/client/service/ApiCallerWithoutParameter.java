package de.segoy.springboottradingibkr.client.service;

import de.segoy.springboottradingdata.model.IBKRDataTypeEntity;

public interface ApiCallerWithoutParameter<T extends IBKRDataTypeEntity> {

    void callApi();
}
