package de.segoy.springboottradingweb.controller.restapicontroller;

import de.segoy.springboottradingibkr.client.config.PropertiesConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NextAvailableOrderIdController {

    private PropertiesConfig propertiesConfig;

    public NextAvailableOrderIdController(PropertiesConfig propertiesConfig) {
        this.propertiesConfig = propertiesConfig;
    }

    @GetMapping("/get-next-order-id")
    public synchronized Integer getNextAvailableOrderId(){
        return propertiesConfig.getNextValidOrderId();
        }
    }
