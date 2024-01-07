package de.segoy.springboottradingweb.controller.restapicontroller;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingweb.service.ResponseMapper;
import org.apache.el.stream.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.OptionalInt;

@Controller
public class NextAvailableOrderIdController {

    private final PropertiesConfig propertiesConfig;
    private final ResponseMapper responseMapper;

    public NextAvailableOrderIdController(PropertiesConfig propertiesConfig, ResponseMapper responseMapper) {
        this.propertiesConfig = propertiesConfig;
        this.responseMapper = responseMapper;
    }

    @GetMapping("/get-next-order-id")
    public ResponseEntity<Integer> getNextAvailableOrderId(){
        return responseMapper.mapResponse(propertiesConfig.getNextValidOrderId().intValue());
        }
    }
