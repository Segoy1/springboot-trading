package de.segoy.springboottradingdata.model;

public class OrderData {
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
