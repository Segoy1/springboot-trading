package de.segoy.springboottradingdata.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountSummaryData extends IBKRDataTypeEntity {

    @Id
    private Long id;

    private String account;
    private String tag;
    private String amount;
    private String currency;

    public Long determineIdByTag() {
        return switch (this.tag) {
            case "AccruedCash" -> -41L;
            case "NetLiquidation" -> -42L;
            case "BuyingPower" -> -43L;
            default -> -44L;
        };
    }
}
