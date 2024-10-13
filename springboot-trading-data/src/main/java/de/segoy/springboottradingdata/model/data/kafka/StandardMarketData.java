package de.segoy.springboottradingdata.model.data.kafka;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StandardMarketData extends KafkaDataType {
    private int tickerId;
    private String field;
    private double price;
    private String attrib;
}
