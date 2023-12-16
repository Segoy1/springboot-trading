package de.segoy.springboottradingdata.model;

import de.segoy.springboottradingdata.model.entity.IBKRDataTypeEntity;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountSummary extends IBKRDataTypeEntity {

    private Long id;
    private String account;
    private String tag;
    private String value;
    private String currency;
}
