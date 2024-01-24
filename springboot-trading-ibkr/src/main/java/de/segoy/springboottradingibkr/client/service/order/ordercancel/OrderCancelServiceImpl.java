package de.segoy.springboottradingibkr.client.service.order.ordercancel;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.model.data.entity.OrderData;
import de.segoy.springboottradingdata.repository.OrderDataRepository;
import de.segoy.springboottradingibkr.client.service.ApiCaller;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
class OrderCancelServiceImpl implements OrderCancelService {

    private final EClientSocket client;
    private final OrderDataRepository orderDataRepository;
    private final ApiCaller<OrderData> orderCancelApiCaller;

    public OrderCancelServiceImpl(EClientSocket client, OrderDataRepository orderDataRepository,
                                  @Qualifier("OrderCancelApiCaller") ApiCaller<OrderData> orderCancelApiCaller) {
        this.client = client;
        this.orderDataRepository = orderDataRepository;
        this.orderCancelApiCaller = orderCancelApiCaller;
    }

    @Override
    public void cancelOrderbyId(Long id) {
        OrderData orderData = orderDataRepository.findById(id).orElseThrow();
        orderCancelApiCaller.callApi(orderData);

    }

    @Override
    public void cancelAllOpenOrders() {
        client.reqGlobalCancel();
    }
}
