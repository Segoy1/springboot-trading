package de.segoy.springboottradingdata.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionData extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //blank for localhost
    private String 	m_retIpAddress;

    //Optional Capabilities
    private String  m_retOptCapts;

    //Port: 7496 for Live 7497 for Paper Trading
    private Integer 	m_retPort;

    //Client Id: default 0
    @Column(name = "client_id")
    private Integer m_retClientId;

    //Field from SampleFrame aka. IBKRConnection
    private Boolean m_bIsFAAccount;

    //Field from SampleFrame aka. IBKRConnection
    private Boolean m_disconnectInProgress;

    private Boolean connected;



}
