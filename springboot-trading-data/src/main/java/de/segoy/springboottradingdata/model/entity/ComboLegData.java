package de.segoy.springboottradingdata.model.entity;

import com.ib.client.Types;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

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
    private Types.Action action; // BUY/SELL/SSHORT/SSHORTX
    private String exchange;
}
