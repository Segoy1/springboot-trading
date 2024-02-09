package de.segoy.springboottradingibkr.client.service;

import de.segoy.springboottradingdata.model.data.IBKRDataType;

import java.util.List;

public interface ListApiResponseChecker<T extends IBKRDataType> {

    public List<T> checkForApiResponseAndUpdate(int id);
}
