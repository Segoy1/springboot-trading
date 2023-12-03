package de.segoy.springboottradingdata.repository;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.ComboLegData;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ComboLegDataRepository  extends CrudRepository<ComboLegData, Integer> {

    public Optional<ComboLegData> findFirstByContractIdAndActionAndRatioAndExchange(Integer contractId, Types.Action action, Integer ratio, String exchange);
}
