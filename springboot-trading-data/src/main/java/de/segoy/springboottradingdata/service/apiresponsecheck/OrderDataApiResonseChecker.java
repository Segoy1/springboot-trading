package de.segoy.springboottradingdata.service.apiresponsecheck;

import de.segoy.springboottradingdata.model.OrderData;
import de.segoy.springboottradingdata.repository.IBKRDataTypeRepository;
import de.segoy.springboottradingdata.repository.message.ErrorMessageRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;
import org.springframework.stereotype.Service;

@Service
public class OrderDataApiResonseChecker extends AbstractApiResponseChecker<OrderData> {

    public OrderDataApiResonseChecker(IBKRDataTypeRepository<OrderData> repository, RepositoryRefreshService repositoryRefreshService, ErrorMessageRepository errorMessageRepository) {
        super(repository, repositoryRefreshService, errorMessageRepository);
    }
}
