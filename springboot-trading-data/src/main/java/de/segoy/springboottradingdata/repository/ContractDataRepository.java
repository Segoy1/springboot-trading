package de.segoy.springboottradingdata.repository;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.ContractData;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ContractDataRepository  extends BaseRepository<ContractData> {

    public List<ContractData> findAllByContractId(Integer id);

    public Optional<ContractData> findFirstByLastTradeDateAndSymbolAndStrikeAndRight(String last, String symbol, BigDecimal strike, Types.Right right);
}
