package de.segoy.springboottradingweb.controller.restapicontroller;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingweb.service.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class NextAvailableOrderIdController {

    private final PropertiesConfig propertiesConfig;
    private final ResponseMapper responseMapper;

    @GetMapping("/get-next-order-id")
    public ResponseEntity<Integer> getNextAvailableOrderId(){
        return responseMapper.mapResponse(propertiesConfig.getNextValidOrderId().intValue());
        }
    }
