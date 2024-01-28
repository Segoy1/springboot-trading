package de.segoy.springboottradingweb.controller.restapicontroller;

import de.segoy.springboottradingdata.model.data.StrategyContractData;
import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import de.segoy.springboottradingibkr.client.strategybuilder.StrategyBuilderService;
import de.segoy.springboottradingweb.service.ResponseMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/contract")
public class ContractDataController {

    private final ContractDataRepository contractDataRepository;
    private final UniqueContractDataProvider uniqueContractDataProvider;
    private final StrategyBuilderService strategyBuilderService;
    private final ResponseMapper responseMapper;

    public ContractDataController(ContractDataRepository contractDataRepository,
                                  UniqueContractDataProvider uniqueContractDataProvider,
                                  StrategyBuilderService strategyBuilderService,
                                  ResponseMapper responseMapper) {
        this.contractDataRepository = contractDataRepository;
        this.uniqueContractDataProvider = uniqueContractDataProvider;
        this.strategyBuilderService = strategyBuilderService;
        this.responseMapper = responseMapper;
    }

    @PostMapping("/combo")
    public ResponseEntity<ContractData> ComboLegContractData(@RequestBody StrategyContractData strategyContractData) {

        Optional<ContractData> savedContract =
                strategyBuilderService.getComboLegContractData(strategyContractData.getContractData(),
                        strategyContractData.getStrategyLegs());

        return responseMapper.mapResponse(savedContract);
    }


    @PostMapping("/single")
    public ResponseEntity<ContractData> saveContractData(@RequestBody ContractData contractData) {
        Optional<ContractData> savedContract = uniqueContractDataProvider.getExistingContractDataOrCallApi(
                contractData);
        return responseMapper.mapResponse(savedContract);
    }

    @GetMapping
    public ResponseEntity<ContractData> getContractDataById(@RequestParam("id") long id) {
        return responseMapper.mapResponse(contractDataRepository.findById(id));
    }
}
