package de.segoy.springboottradingibkr.client.service;

import de.segoy.springboottradingdata.model.entity.IBKRDataTypeEntity;

public interface ApiCaller<T extends IBKRDataTypeEntity> {

    void callApi(T entity);
}
