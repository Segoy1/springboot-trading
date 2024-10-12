package de.segoy.springboottradingdata.modelsynchronize;

import com.ib.client.Contract;
import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ComboLegDataDBO;
import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
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


    public ContractDataDBO findInDBOrConvertAndSaveOrUpdateIfIdIsProvided(OptionalLong id, Contract contract){
        boolean isCombo = Types.SecType.BAG.name().equals(contract.getSecType());
        if(!isCombo && contractDataRepository.findFirstByContractId(contract.conid()).isPresent()){
            return contractDataRepository.findFirstByContractId(contract.conid()).get();
        } else {
            List<ComboLegDataDBO> comboLegs = new ArrayList<>();
            ContractDataDBO newContractDataDBO = ibkrContractToContractData.convertIBKRContract(contract);

            if(isCombo){
            newContractDataDBO.getComboLegs().forEach(
                    (comboLeg)-> {
                    comboLegs.add(saveComboLegIfNotExistent(comboLeg));
                    });
            newContractDataDBO.setComboLegs(comboLegs);
            id = id.isPresent()?id:comboContractDataFinder.checkContractWithComboLegs(comboLegs);
            }
            id.ifPresent(newContractDataDBO::setId);
            return contractDataRepository.save(newContractDataDBO);
        }
    }
    private ComboLegDataDBO saveComboLegIfNotExistent(ComboLegDataDBO comboLegDataDBO){
       return comboLegDataRepository.findFirstByContractIdAndActionAndRatioAndExchange(
                comboLegDataDBO.getContractId(),
                comboLegDataDBO.getAction(),
                comboLegDataDBO.getRatio(),
                comboLegDataDBO.getExchange()).orElseGet(()-> comboLegDataRepository.save(comboLegDataDBO));
    }
}
