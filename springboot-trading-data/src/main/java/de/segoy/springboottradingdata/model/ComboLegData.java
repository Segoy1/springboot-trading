package de.segoy.springboottradingdata.model;

import de.segoy.springboottradingdata.type.Action;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComboLegData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer contractId;
    private Integer ratio;

    @Enumerated(EnumType.STRING)
    private Action action; // BUY/SELL/SSHORT/SSHORTX
    private String exchange;
}
