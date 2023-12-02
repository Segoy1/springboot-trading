package de.segoy.springboottradingdata.service;

import de.segoy.springboottradingdata.model.message.ErrorMessage;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import de.segoy.springboottradingdata.repository.OrderDataRepository;
import de.segoy.springboottradingdata.repository.message.ErrorMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ErrorMessageHandler {

    //Need to put in the Repos manually (Maybe fix with Generic?)
    private final ContractDataRepository contractDataRepository;
    private final OrderDataRepository orderDataRepository;
    private final ErrorMessageRepository errorMessageRepository;

    public ErrorMessageHandler(ContractDataRepository contractDataRepository, OrderDataRepository orderDataRepository, ErrorMessageRepository errorMessageRepository) {
        this.contractDataRepository = contractDataRepository;
        this.orderDataRepository = orderDataRepository;
        this.errorMessageRepository = errorMessageRepository;
    }

    public void handleError(int id, String message) {
        log.warn(message);
        errorMessageRepository.save(ErrorMessage.builder().id(id).message(message).build());

        orderDataRepository.findById(id).ifPresent((orderData -> {
            orderData.setTouchedByApi(true);
            orderDataRepository.save(orderData);
        }));

        contractDataRepository.findById(id).ifPresent((contractData -> {
            contractData.setTouchedByApi(true);
            contractDataRepository.save(contractData);
        }));
    }
}