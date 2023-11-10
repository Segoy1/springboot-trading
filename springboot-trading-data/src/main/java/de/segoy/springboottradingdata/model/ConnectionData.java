package de.segoy.springboottradingdata.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //blank for localhost
    private String 	m_retIpAddress;

    //Optional Capabilities
    private String  m_retOptCapts;

    //Port: 7496 for Live 7497 for Paper Trading
    private int 	m_retPort;

    //Client Id: default 0
    private int 	m_retClientId;


}
