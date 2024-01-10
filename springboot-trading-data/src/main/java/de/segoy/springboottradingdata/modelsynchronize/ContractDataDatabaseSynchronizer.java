package de.segoy.springboottradingdata.modelsynchronize;

import com.ib.client.Contract;
import com.ib.client.Types;
import de.segoy.springboottradingdata.model.entity.ComboLegData;
import de.segoy.springboottradingdata.model.entity.ContractData;
import de.segoy.springboottradingdata.modelconverter.IBKRContractToContractData;
import de.segoy.springboottradingdata.repository.ComboLegDataRepository;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ContractDataDatabaseSynchronizer {

    private final IBKRContractToContractData ibkrContractToContractData;
    private final ContractDataRepository contractDataRepository;
    private final ComboLegDataRepository comboLegDataRepository;

    public ContractDataDatabaseSynchronizer(IBKRContractToContractData ibkrContractToContractData, ContractDataRepository contractDataRepository, ComboLegDataRepository comboLegDataRepository) {
        this.ibkrContractToContractData = ibkrContractToContractData;
        this.contractDataRepository = contractDataRepository;
        this.comboLegDataRepository = comboLegDataRepository;
    }

    public ContractData findInDBOrConvertAndSaveOrUpdateIfIdIsProvided(OptionalLong id, Contract contract){
        boolean isCombo = Types.SecType.BAG.name().equals(contract.getSecType());
        if(contractDataRepository.findFirstByContractId(contract.conid()).isPresent() && !isCombo){
            return contractDataRepository.findFirstByContractId(contract.conid()).get();
        } else {
            List<ComboLegData> comboLegs = new ArrayList<>();
            ContractData newContractData = ibkrContractToContractData.convertIBKRContract(contract);

            id.ifPresent(newContractData::setId);
            if(isCombo){
            newContractData.getComboLegs().forEach(
                    (comboLeg)-> {
                    comboLegs.add(saveComboLegIfNotExistent(comboLeg));
                    });
            newContractData.setComboLegs(comboLegs);
            newContractData = checkContractWithComboLegs(comboLegs).orElse(newContractData);
            }
            return contractDataRepository.save(newContractData);
        }
    }
    private ComboLegData saveComboLegIfNotExistent(ComboLegData comboLegData){
       return comboLegDataRepository.findFirstByContractIdAndActionAndRatioAndExchange(
                comboLegData.getContractId(),
                comboLegData.getAction(),
                comboLegData.getRatio(),
                comboLegData.getExchange()).orElseGet(()->{
            return comboLegDataRepository.save(comboLegData);
        });
    }
    private Optional<ContractData> checkContractWithComboLegs(List<ComboLegData> comboLegs){
        final List<ContractData> oldContracts= new ArrayList<>();
        for(ComboLegData comboLeg : comboLegs){
            List<ContractData> contracts =
                    contractDataRepository.findByComboLegsDescriptionContains(comboLeg.getContractId().toString());
            if(contracts.isEmpty()){
                //if none are found on any Given ComboLeg there is no existing Order
                return Optional.empty();
            }else if(oldContracts.isEmpty()){
                //populate List of existing Orders with first iteration
                oldContracts.addAll(contracts);
            }else{
                //remove all existing Orders that are not in latest iteration
                oldContracts.removeIf(oldContract -> !contracts.contains(oldContract));
                if(oldContracts.isEmpty()){
                    return Optional.empty();
                }
            }
        };
        //if list is not empty return first value, there should never be more than one
        return Optional.of(oldContracts.get(0));
    }
}
