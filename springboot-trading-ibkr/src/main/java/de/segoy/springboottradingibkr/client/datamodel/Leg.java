package de.segoy.springboottradingibkr.client.datamodel;

import com.ib.client.Types;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Leg {

    private Types.Right right;
    private Types.Action action;
    private Integer ratio;
    private double strike;

}
