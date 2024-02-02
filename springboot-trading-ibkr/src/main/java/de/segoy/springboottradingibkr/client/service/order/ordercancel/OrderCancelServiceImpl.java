package de.segoy.springboottradingibkr.client.service.order.ordercancel;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.model.data.entity.OrderData;
import de.segoy.springboottradingdata.repository.OrderDataRepository;
import de.segoy.springboottradingibkr.client.service.ApiCaller;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class OrderCancelServiceImpl implements OrderCancelService {

    private final EClientSocket client;
    private final OrderDataRepository orderDataRepository;
    private final @Qualifier("OrderCancelApiCaller") ApiCaller<OrderData> orderCancelApiCaller;

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
