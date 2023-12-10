package de.segoy.springboottradingdata.model;

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
