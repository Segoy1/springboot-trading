package de.segoy.springboottradingdata.model;

import com.ib.client.Contract;
import com.ib.client.Decimal;

public class AccountModel {
    public void updateAccountValue(String key, String value, String currency, String accountName) {
        //TODO Implement
    }

    public void updatePortfolio(Contract contract, Decimal position, double marketPrice, double marketValue, double averageCost, double unrealizedPNL, double realizedPNL, String accountName) {
        //TODO Implement
    }

    public void updateAccountTime(String timeStamp) {
        //TODO Implement
    }

    public void accountDownloadEnd(String accountName) {
        //TODO Implement
    }
}
