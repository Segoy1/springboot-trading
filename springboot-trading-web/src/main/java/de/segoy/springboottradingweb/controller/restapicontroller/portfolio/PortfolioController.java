package de.segoy.springboottradingweb.controller.restapicontroller.portfolio;

import de.segoy.springboottradingdata.model.entity.PositionData;
import de.segoy.springboottradingibkr.client.service.position.PositionService;
import de.segoy.springboottradingibkr.client.service.position.profitandloss.PositionPnLService;
import de.segoy.springboottradingweb.service.ResponseMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    private final PositionService positionService;
    private final ResponseMapper responseMapper;
    private final PositionPnLService positionPnLService;

    public PortfolioController(PositionService positionService, ResponseMapper responseMapper, PositionPnLService positionPnLService) {
        this.positionService = positionService;
        this.responseMapper = responseMapper;
        this.positionPnLService = positionPnLService;
    }

    @GetMapping
    public ResponseEntity<List<PositionData>> getPortfolio(){
        return responseMapper.mapResponse(positionService.getUpdatedPortfolio());
    }
    @GetMapping("/pnl")
    public void getPnL(){
        positionPnLService.getPortfolioPnL();
    }
    @GetMapping("/pnl/cancel")
    public void cancelPnL(){
        positionPnLService.cancelPortfolioPnL();
    }
    @GetMapping("/pnl-single")
    public void getPnLSingle(@RequestParam(name = "id") int id){
        positionPnLService.getSinglePnL(id);
    }
    @GetMapping("/pnl-single/cancel")
    public void cancelPnLSingle(@RequestParam(name="id") int id){
        positionPnLService.cancelSinglePnL(id);
    }
}
