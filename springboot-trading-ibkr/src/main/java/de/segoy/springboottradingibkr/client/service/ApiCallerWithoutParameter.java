package de.segoy.springboottradingibkr.client.service;

import de.segoy.springboottradingdata.model.IBKRDataTypeEntity;

public interface ApiCallerWithoutParameter<T extends IBKRDataTypeEntity> {

    public void callApi();
}
