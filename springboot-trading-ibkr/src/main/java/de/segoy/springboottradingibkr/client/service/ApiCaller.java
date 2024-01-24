package de.segoy.springboottradingibkr.client.service;

import de.segoy.springboottradingdata.model.data.IBKRDataType;

public interface ApiCaller<T extends IBKRDataType> {

    void callApi(T entity);
}
