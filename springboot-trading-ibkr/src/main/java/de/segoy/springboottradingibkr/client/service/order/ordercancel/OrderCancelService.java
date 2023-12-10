package de.segoy.springboottradingibkr.client.service.order.ordercancel;

public interface OrderCancelService {

    void cancelOrderbyId(Long id);
    void cancelAllOpenOrders();
}
