package de.segoy.springboottradingdata.service.apiresponsecheck;

import de.segoy.springboottradingdata.model.HistoricalMarketData;
import de.segoy.springboottradingdata.repository.IBKRDataTypeRepository;
import de.segoy.springboottradingdata.repository.message.ErrorMessageRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;

public class HistoricalDataApiResponseChecker extends AbstractApiResponseChecker<HistoricalMarketData> {

    public HistoricalDataApiResponseChecker(IBKRDataTypeRepository<HistoricalMarketData> repository, RepositoryRefreshService repositoryRefreshService, ErrorMessageRepository errorMessageRepository) {
        super(repository, repositoryRefreshService, errorMessageRepository);
    }

    @Override
    protected boolean notInRepositoryOrError(Long id) {
        return repository.findByContractDataId(id.intValue()).isEmpty() && errorMessageRepository.findAllByErrorId(id.intValue()).isEmpty();
    }
}
