package de.segoy.springboottradingibkr.model;

public class OrderModel {
    private boolean isFAAccount;


    public void setIdAtLeast(int orderId) {
    }

    public void setFAAccount(boolean isFAAccount) {
        this.isFAAccount = isFAAccount;
    }

    public boolean isFAAccount() {
        return isFAAccount;
    }
}
