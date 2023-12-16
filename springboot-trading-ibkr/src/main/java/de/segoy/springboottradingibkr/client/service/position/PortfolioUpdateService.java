package de.segoy.springboottradingibkr.client.service.position;

import de.segoy.springboottradingdata.model.entity.PositionData;
import de.segoy.springboottradingdata.repository.PositionDataRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PortfolioUpdateService {
    private final PositionDataRepository positionDataRepository;

    public PortfolioUpdateService(PositionDataRepository positionDataRepository) {
        this.positionDataRepository = positionDataRepository;
    }

    public List<PositionData> updatePortfolio(List<PositionData> positions) {
        List<PositionData> updatedPortfolio = new ArrayList<>();
        positionDataRepository.findAll().forEach((positionData) -> {
            if (!positions.contains(positionData)) {
                positionDataRepository.delete(positionData);
            }else{
                updatedPortfolio.add(positionData);
            }
        });
        return updatedPortfolio;
    }
}