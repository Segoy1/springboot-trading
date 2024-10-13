package de.segoy.springboottradingdata.repository;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ComboLegDbo;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ComboLegRepository extends CrudRepository<ComboLegDbo, Integer> {

    public Optional<ComboLegDbo> findFirstByContractIdAndActionAndRatioAndExchange(Integer contractId, Types.Action action, Integer ratio, String exchange);
}
