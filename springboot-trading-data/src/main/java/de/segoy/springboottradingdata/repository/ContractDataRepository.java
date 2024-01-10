package de.segoy.springboottradingdata.repository;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.entity.ComboLegData;
import de.segoy.springboottradingdata.model.entity.ContractData;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ContractDataRepository  extends IBKRDataTypeRepository<ContractData> {

    List<ContractData> findAllByContractId(Integer id);
    Optional<ContractData> findFirstByContractId(Integer id);
    List<ContractData> findByComboLegsContains(ComboLegData comboLegData);
    List<ContractData> findByComboLegsDescriptionContains(String contractId);

    Optional<ContractData> findFirstBySymbolAndSecurityTypeAndCurrency(String symbol, Types.SecType secType,
                                                                       String currency);

    Optional<ContractData> findFirstByLastTradeDateAndSymbolAndStrikeAndRight(String last, String symbol, BigDecimal strike, Types.Right right);

//    Optional<ContractData> findByLocalSymbol

    List<ContractData> findAllBySecurityType(Types.SecType type);

    //Todo: find cleaner solution wich does not increment the sequence
    @Query(value="select nextval('contract_id_sequence')", nativeQuery = true)
    int nextValidId();
}
