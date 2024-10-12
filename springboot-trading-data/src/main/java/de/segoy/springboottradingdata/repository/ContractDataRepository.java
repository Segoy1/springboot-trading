package de.segoy.springboottradingdata.repository;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ContractDataRepository  extends IBKRDataTypeRepository<ContractDataDBO> {

    List<ContractDataDBO> findAllByContractId(Integer id);
    Optional<ContractDataDBO> findFirstByContractId(Integer id);
    List<ContractDataDBO> findByComboLegsDescriptionContains(String contractId);

    Optional<ContractDataDBO> findFirstBySymbolAndSecurityTypeAndCurrency(Symbol symbol, Types.SecType secType,
                                                                          String currency);

    Optional<ContractDataDBO> findFirstByLastTradeDateAndSymbolAndStrikeAndRight(String last, Symbol symbol, BigDecimal strike, Types.Right right);

//    Optional<ContractData> findByLocalSymbol

    List<ContractDataDBO> findAllBySecurityType(Types.SecType type);

    //Todo: find cleaner solution wich does not increment the sequence
    @Query(value="select nextval('contract_id_sequence')", nativeQuery = true)
    int nextValidId();
}
