package de.segoy.springboottradingdata.repository;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ContractRepository extends IBKRDataTypeRepository<ContractDbo> {

    List<ContractDbo> findAllByContractId(Integer id);
    Optional<ContractDbo> findFirstByContractId(Integer id);
    List<ContractDbo> findByComboLegsDescriptionContains(String contractId);

    Optional<ContractDbo> findFirstBySymbolAndSecurityTypeAndCurrency(Symbol symbol, Types.SecType secType,
                                                                      String currency);

    Optional<ContractDbo> findFirstByLastTradeDateAndSymbolAndStrikeAndRight(String last, Symbol symbol, BigDecimal strike, Types.Right right);

//    Optional<ContractData> findByLocalSymbol

    List<ContractDbo> findAllBySecurityType(Types.SecType type);

    //Todo: find cleaner solution wich does not increment the sequence
    @Query(value="select nextval('contract_id_sequence')", nativeQuery = true)
    int nextValidId();
}
