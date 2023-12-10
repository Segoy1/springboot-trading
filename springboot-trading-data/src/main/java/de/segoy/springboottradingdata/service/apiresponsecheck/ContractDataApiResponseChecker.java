package de.segoy.springboottradingdata.service.apiresponsecheck;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.repository.IBKRDataTypeRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;
import org.springframework.stereotype.Service;

@Service
public class ContractDataApiResponseChecker extends AbstractApiResponseChecker<ContractData> {

    public ContractDataApiResponseChecker(IBKRDataTypeRepository<ContractData> repository, RepositoryRefreshService repositoryRefreshService, PropertiesConfig propertiesConfig) {
        super(repository,repositoryRefreshService, propertiesConfig);
    }
}
