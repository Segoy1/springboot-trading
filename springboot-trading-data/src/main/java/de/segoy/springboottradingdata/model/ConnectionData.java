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
    private Long id;

    //blank for localhost
    private String ipAddress;

    //Optional Capabilities
    private String optCapts;

    //Port: 7496 for Live 7497 for Paper Trading
    private Integer port;

    //Client Id: default 0
    @Column(name = "client_id")
    private Integer clientId;

    //Field from SampleFrame aka. IBKRConnection
    private Boolean isFAAccount;

    //Field from SampleFrame aka. IBKRConnection
    private Boolean disconnectInProgress;

    private Boolean connected;



}
