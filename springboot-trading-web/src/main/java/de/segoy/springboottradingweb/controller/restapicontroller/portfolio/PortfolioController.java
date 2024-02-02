package de.segoy.springboottradingweb.controller.restapicontroller.portfolio;

import de.segoy.springboottradingibkr.client.service.position.PositionService;
import de.segoy.springboottradingibkr.client.service.position.profitandloss.PositionPnLService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/portfolio")
@RequiredArgsConstructor
public class PortfolioController {

    private final PositionService positionService;
    private final PositionPnLService positionPnLService;

    @GetMapping("/positions")
    public void getPortfolio(){
        positionService.callPortfolio();
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
