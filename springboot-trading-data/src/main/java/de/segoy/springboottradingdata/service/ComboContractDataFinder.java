package de.segoy.springboottradingdata.service;

import de.segoy.springboottradingdata.model.data.entity.ComboLegData;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

@Service
@RequiredArgsConstructor
public class ComboContractDataFinder {

    private final ContractDataRepository contractDataRepository;


    public OptionalLong checkContractWithComboLegs(List<ComboLegData> comboLegs){
        final List<Long> oldContracts= new ArrayList<>();
        for(ComboLegData comboLeg : comboLegs){
            List<Long> contracts = new ArrayList<>();
            contractDataRepository.findByComboLegsDescriptionContains(comboLeg.getContractId().toString()).forEach((contractData)->{
                contracts.add(contractData.getId());
            });
            if(contracts.isEmpty()){
                //if none are found on any Given ComboLeg there is no existing Order
                return OptionalLong.empty();
            }else if(oldContracts.isEmpty()){
                //populate List of existing Orders with first iteration
                oldContracts.addAll(contracts);
            }else{
                //remove all existing Orders that are not in latest iteration
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
