package de.segoy.springboottradingdata.modelsynchronize;

import com.ib.client.Contract;
import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ComboLegDataDBO;
import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.modelconverter.IBKRContractToContractData;
import de.segoy.springboottradingdata.repository.ComboLegDataRepository;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import de.segoy.springboottradingdata.service.ComboContractDataFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractDataDBODatabaseSynchronizerTest {

    @Mock
    private IBKRContractToContractData ibkrContractToContractData;
    @Mock
    private ContractDataRepository contractDataRepository;
    @Mock
    private ComboLegDataRepository comboLegDataRepository;
    @Mock
    private ComboContractDataFinder comboContractDataFinder;

    @InjectMocks
    private ContractDataDatabaseSynchronizer contractDataDatabaseSynchronizer;


    @BeforeEach
    void setUp() {
    }

    @Test
    void testNOComboYESExistent(){
        Contract contract = new Contract();
        contract.conid(1);
        contract.secType(Types.SecType.OPT);

        ContractDataDBO data = ContractDataDBO.builder().contractId(1).securityType(Types.SecType.OPT).build();
        Optional<ContractDataDBO> dataOpt =  Optional.of(data);

        when(contractDataRepository.findFirstByContractId(1)).thenReturn(dataOpt);

        assertEquals(data,
                contractDataDatabaseSynchronizer.findInDBOrConvertAndSaveOrUpdateIfIdIsProvided(OptionalLong.empty(),
                        contract));
        verify(contractDataRepository, times(2)).findFirstByContractId(1);
    }

    //TODO incorrect data where there are Comboleg but is no combo
    @Test
    void testNOComboNONExistent(){
        Contract contract = new Contract();
        contract.conid(1);
        contract.secType(Types.SecType.OPT);

        ContractDataDBO data = ContractDataDBO.builder().contractId(1).securityType(Types.SecType.OPT).build();

        when(contractDataRepository.findFirstByContractId(1)).thenReturn(Optional.empty());
        when(ibkrContractToContractData.convertIBKRContract(contract)).thenReturn(data);
        when(contractDataRepository.save(data)).thenReturn(data);

        assertEquals(data,
                contractDataDatabaseSynchronizer.findInDBOrConvertAndSaveOrUpdateIfIdIsProvided(OptionalLong.empty(),
                        contract));
        verify(contractDataRepository, times(1)).findFirstByContractId(1);
        verify(ibkrContractToContractData, times(1)).convertIBKRContract(contract);
        verify(contractDataRepository, times(1)).save(data);
    }
    @Test
    void testNOComboNONExistentWithId(){
        Contract contract = new Contract();
        contract.conid(1);
        contract.secType(Types.SecType.OPT);

        ContractDataDBO data = ContractDataDBO.builder().contractId(1).securityType(Types.SecType.OPT).build();

        when(contractDataRepository.findFirstByContractId(1)).thenReturn(Optional.empty());
        when(ibkrContractToContractData.convertIBKRContract(contract)).thenReturn(data);
        when(contractDataRepository.save(data)).thenReturn(data);

        ContractDataDBO result =
                contractDataDatabaseSynchronizer.findInDBOrConvertAndSaveOrUpdateIfIdIsProvided(OptionalLong.of(12L),
                contract);

        assertEquals(1, result.getContractId());
        assertEquals(Types.SecType.OPT, result.getSecurityType());
        assertEquals(12L, result.getId());
        verify(contractDataRepository, times(1)).findFirstByContractId(1);
        verify(ibkrContractToContractData, times(1)).convertIBKRContract(contract);
        verify(contractDataRepository, times(1)).save(data);
    }
    @Test
    void testYESComboNONExistentWithID(){
        Contract contract = new Contract();
        contract.conid(1);
        contract.secType(Types.SecType.BAG);

        ComboLegDataDBO leg1 =  ComboLegDataDBO.builder().ratio(1).action(Types.Action.BUY).exchange("TEST").contractId(123).build();
        ComboLegDataDBO leg2 =
                ComboLegDataDBO.builder().ratio(2).action(Types.Action.SELL).exchange("TEST").contractId(124).build();
        ContractDataDBO data =
                ContractDataDBO.builder().contractId(1).securityType(Types.SecType.BAG).comboLegs(List.of(leg1,leg2)).build();


        when(ibkrContractToContractData.convertIBKRContract(contract)).thenReturn(data);
        when(contractDataRepository.save(data)).thenReturn(data);
        when(comboLegDataRepository.findFirstByContractIdAndActionAndRatioAndExchange(leg1.getContractId(),
                leg1.getAction(), leg1.getRatio(), leg1.getExchange())).thenReturn(Optional.empty());
        when(comboLegDataRepository.findFirstByContractIdAndActionAndRatioAndExchange(leg2.getContractId(),
                leg2.getAction(), leg2.getRatio(), leg2.getExchange())).thenReturn(Optional.of(leg2));
        when(comboLegDataRepository.save(leg1)).thenReturn(leg1);

        ContractDataDBO result =
                contractDataDatabaseSynchronizer.findInDBOrConvertAndSaveOrUpdateIfIdIsProvided(OptionalLong.of(12L),
                        contract);


        assertEquals(leg1,result.getComboLegs().get(0));
        assertEquals(leg2,result.getComboLegs().get(1));
        assertEquals(1,result.getContractId());
        assertEquals(Types.SecType.BAG,result.getSecurityType());


        verify(ibkrContractToContractData, times(1)).convertIBKRContract(contract);
        verify(contractDataRepository, times(1)).save(data);
        verify(comboLegDataRepository, times(1)).save(leg1);
        verify(comboLegDataRepository, times(1)).findFirstByContractIdAndActionAndRatioAndExchange(leg2.getContractId(),
                leg2.getAction(), leg2.getRatio(), leg2.getExchange());
        verify(comboLegDataRepository, times(1)).findFirstByContractIdAndActionAndRatioAndExchange(leg1.getContractId(),
                leg1.getAction(), leg1.getRatio(), leg1.getExchange());
    }
    @Test
    void testYESComboNONExistentWithoutID(){
        Contract contract = new Contract();
        contract.conid(1);
        contract.secType(Types.SecType.BAG);

        ComboLegDataDBO leg1 =  ComboLegDataDBO.builder().ratio(1).action(Types.Action.BUY).exchange("TEST").contractId(123).build();
        ComboLegDataDBO leg2 =
                ComboLegDataDBO.builder().ratio(2).action(Types.Action.SELL).exchange("TEST").contractId(124).build();
        List<ComboLegDataDBO> comboLegs = List.of(leg1, leg2);
        ContractDataDBO data =
                ContractDataDBO.builder().contractId(1).securityType(Types.SecType.BAG).comboLegs(comboLegs).build();


        when(ibkrContractToContractData.convertIBKRContract(contract)).thenReturn(data);
        when(contractDataRepository.save(data)).thenReturn(data);
        when(comboLegDataRepository.findFirstByContractIdAndActionAndRatioAndExchange(leg1.getContractId(),
                leg1.getAction(), leg1.getRatio(), leg1.getExchange())).thenReturn(Optional.empty());
        when(comboLegDataRepository.findFirstByContractIdAndActionAndRatioAndExchange(leg2.getContractId(),
                leg2.getAction(), leg2.getRatio(), leg2.getExchange())).thenReturn(Optional.of(leg2));
        when(comboLegDataRepository.save(leg1)).thenReturn(leg1);
        when(comboContractDataFinder.checkContractWithComboLegs(comboLegs)).thenReturn(OptionalLong.empty());

        ContractDataDBO result =
                contractDataDatabaseSynchronizer.findInDBOrConvertAndSaveOrUpdateIfIdIsProvided(OptionalLong.empty(),
                        contract);


        assertEquals(leg1,result.getComboLegs().get(0));
        assertEquals(leg2,result.getComboLegs().get(1));
        assertEquals(1,result.getContractId());
        assertEquals(Types.SecType.BAG,result.getSecurityType());
        assertEquals(null, result.getId());


        verify(ibkrContractToContractData, times(1)).convertIBKRContract(contract);
        verify(contractDataRepository, times(1)).save(data);
        verify(comboLegDataRepository, times(1)).save(leg1);
        verify(comboLegDataRepository, times(1)).findFirstByContractIdAndActionAndRatioAndExchange(leg2.getContractId(),
                leg2.getAction(), leg2.getRatio(), leg2.getExchange());
        verify(comboLegDataRepository, times(1)).findFirstByContractIdAndActionAndRatioAndExchange(leg1.getContractId(),
                leg1.getAction(), leg1.getRatio(), leg1.getExchange());
    }


    @Test
    void testYESComboYESExistent(){
        Contract contract = new Contract();
        contract.conid(1);
        contract.secType(Types.SecType.BAG);

        ComboLegDataDBO leg1 =  ComboLegDataDBO.builder().ratio(1).action(Types.Action.BUY).exchange("TEST").contractId(123).build();
        ComboLegDataDBO leg2 =
                ComboLegDataDBO.builder().ratio(2).action(Types.Action.SELL).exchange("TEST").contractId(124).build();
        List<ComboLegDataDBO> comboLegs = List.of(leg1, leg2);
        ContractDataDBO data =
                ContractDataDBO.builder().contractId(1).securityType(Types.SecType.BAG).comboLegs(comboLegs).build();


        when(ibkrContractToContractData.convertIBKRContract(contract)).thenReturn(data);
        when(contractDataRepository.save(data)).thenReturn(data);
        when(comboLegDataRepository.findFirstByContractIdAndActionAndRatioAndExchange(leg1.getContractId(),
                leg1.getAction(), leg1.getRatio(), leg1.getExchange())).thenReturn(Optional.empty());
        when(comboLegDataRepository.findFirstByContractIdAndActionAndRatioAndExchange(leg2.getContractId(),
                leg2.getAction(), leg2.getRatio(), leg2.getExchange())).thenReturn(Optional.of(leg2));
        when(comboLegDataRepository.save(leg1)).thenReturn(leg1);
        when(comboContractDataFinder.checkContractWithComboLegs(comboLegs)).thenReturn(OptionalLong.of(1234));

        ContractDataDBO result =
                contractDataDatabaseSynchronizer.findInDBOrConvertAndSaveOrUpdateIfIdIsProvided(OptionalLong.empty(),
                        contract);


        assertEquals(leg1,result.getComboLegs().get(0));
        assertEquals(leg2,result.getComboLegs().get(1));
        assertEquals(1,result.getContractId());
        assertEquals(Types.SecType.BAG,result.getSecurityType());
        assertEquals(1234, result.getId());


        verify(ibkrContractToContractData, times(1)).convertIBKRContract(contract);
        verify(contractDataRepository, times(1)).save(data);
        verify(comboLegDataRepository, times(1)).save(leg1);
        verify(comboLegDataRepository, times(1)).findFirstByContractIdAndActionAndRatioAndExchange(leg2.getContractId(),
                leg2.getAction(), leg2.getRatio(), leg2.getExchange());
        verify(comboLegDataRepository, times(1)).findFirstByContractIdAndActionAndRatioAndExchange(leg1.getContractId(),
                leg1.getAction(), leg1.getRatio(), leg1.getExchange());
    }


}
