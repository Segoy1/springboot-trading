package de.segoy.springboottradingdata.repository;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ComboLegDataDBO;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ComboLegDataRepository  extends CrudRepository<ComboLegDataDBO, Integer> {

    public Optional<ComboLegDataDBO> findFirstByContractIdAndActionAndRatioAndExchange(Integer contractId, Types.Action action, Integer ratio, String exchange);
}
