package de.segoy.springboottradingibkr.client.service.position.profitandloss;

import de.segoy.springboottradingdata.model.entity.ProfitAndLossData;
import de.segoy.springboottradingdata.repository.PositionDataRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PositionPnLService {

    private final PositionDataRepository positionDataRepository;
    private final SinglePnLService singlePnLService;

    public PositionPnLService(PositionDataRepository positionDataRepository, SinglePnLService singlePnLService) {
        this.positionDataRepository = positionDataRepository;
        this.singlePnLService = singlePnLService;
    }

    public List<ProfitAndLossData> getPortfolioPnL(){
        List<ProfitAndLossData> pnlData = new ArrayList<>();
        positionDataRepository.findAll().forEach((positionData -> {
            singlePnLService.getProfitAndLossData(positionData).ifPresent((pnlData::add));
        }));
        return pnlData;
    }
}
