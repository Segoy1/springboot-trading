package de.segoy.springboottradingweb.controller.restapicontroller;

import de.segoy.springboottradingdata.model.data.StrategyContractData;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.repository.ContractRepository;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import de.segoy.springboottradingibkr.client.strategybuilder.StrategyBuilderService;
import de.segoy.springboottradingweb.service.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/contract")
@RequiredArgsConstructor
public class ContractDataController {

    private final ContractRepository contractRepository;
    private final UniqueContractDataProvider uniqueContractDataProvider;
    private final StrategyBuilderService strategyBuilderService;
    private final ResponseMapper responseMapper;

    @PostMapping("/combo")
    public ResponseEntity<ContractDbo> ComboLegContractData(@RequestBody StrategyContractData strategyContractData) {

        Optional<ContractDbo> savedContract =
                strategyBuilderService.getComboLegContractData(strategyContractData);

        return responseMapper.mapResponse(savedContract);
    }


    @PostMapping("/single")
    public ResponseEntity<ContractDbo> saveContractData(@RequestBody ContractDbo contractDBO) {
        Optional<ContractDbo> savedContract = uniqueContractDataProvider.getExistingContractDataOrCallApi(
                contractDBO);
        return responseMapper.mapResponse(savedContract);
    }

    @GetMapping
    public ResponseEntity<ContractDbo> getContractDataById(@RequestParam("id") long id) {
        return responseMapper.mapResponse(contractRepository.findById(id));
    }
    @GetMapping("/contract-id")
    public ResponseEntity<ContractDbo> getContractDataByContractId(@RequestParam("id") int id) {
        return responseMapper.mapResponse(contractRepository.findFirstByContractId(id));
    }
}
