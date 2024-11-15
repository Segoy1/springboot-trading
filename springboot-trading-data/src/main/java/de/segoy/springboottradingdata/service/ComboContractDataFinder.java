package de.segoy.springboottradingdata.service;

import de.segoy.springboottradingdata.model.data.entity.ComboLegDbo;
import de.segoy.springboottradingdata.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

@Service
@RequiredArgsConstructor
public class ComboContractDataFinder {

    private final ContractRepository contractRepository;


    public OptionalLong checkContractWithComboLegs(List<ComboLegDbo> comboLegs){
        final List<Long> oldContracts= new ArrayList<>();
        for(ComboLegDbo comboLeg : comboLegs){
            List<Long> contracts = new ArrayList<>();
            contractRepository.findByComboLegsDescriptionContains(comboLeg.getContractId().toString()).forEach((contractData)->{
                contracts.add(contractData.getId());
            });
            if(contracts.isEmpty()){
                //if none are found on any Given ComboLeg there is no existing Contract
                return OptionalLong.empty();
            }else if(oldContracts.isEmpty()){
                //populate List of existing Contracts with first iteration
                oldContracts.addAll(contracts);
            }else{
                //remove all existing Contracts that are not in latest iteration
                oldContracts.removeIf(contract -> !contracts.contains(contract));

                if(oldContracts.isEmpty()){
                    return OptionalLong.empty();
                }
            }
        };
        //if list is not empty return first value, there should never be more than one
        return OptionalLong.of(oldContracts.get(0));
    }
}
