package de.segoy.springboottradingdata.modelsynchronize;

import com.ib.client.Contract;
import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ComboLegData;
import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingdata.modelconverter.IBKRContractToContractData;
import de.segoy.springboottradingdata.repository.ComboLegDataRepository;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import de.segoy.springboottradingdata.service.ComboContractDataFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class ContractDataDatabaseSynchronizer {

    private final IBKRContractToContractData ibkrContractToContractData;
    private final ContractDataRepository contractDataRepository;
    private final ComboLegDataRepository comboLegDataRepository;
    private final ComboContractDataFinder comboContractDataFinder;


    public ContractData findInDBOrConvertAndSaveOrUpdateIfIdIsProvided(OptionalLong id, Contract contract){
        boolean isCombo = Types.SecType.BAG.name().equals(contract.getSecType());
        if(!isCombo && contractDataRepository.findFirstByContractId(contract.conid()).isPresent()){
            return contractDataRepository.findFirstByContractId(contract.conid()).get();
        } else {
            List<ComboLegData> comboLegs = new ArrayList<>();
            ContractData newContractData = ibkrContractToContractData.convertIBKRContract(contract);

            if(isCombo){
            newContractData.getComboLegs().forEach(
                    (comboLeg)-> {
                    comboLegs.add(saveComboLegIfNotExistent(comboLeg));
                    });
            newContractData.setComboLegs(comboLegs);
            id = id.isPresent()?id:comboContractDataFinder.checkContractWithComboLegs(comboLegs);
            }
            id.ifPresent(newContractData::setId);
            return contractDataRepository.save(newContractData);
        }
    }
    private ComboLegData saveComboLegIfNotExistent(ComboLegData comboLegData){
       return comboLegDataRepository.findFirstByContractIdAndActionAndRatioAndExchange(
                comboLegData.getContractId(),
                comboLegData.getAction(),
                comboLegData.getRatio(),
                comboLegData.getExchange()).orElseGet(()-> comboLegDataRepository.save(comboLegData));
    }
}
