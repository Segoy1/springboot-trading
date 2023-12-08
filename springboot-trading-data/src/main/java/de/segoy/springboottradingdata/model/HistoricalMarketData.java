package de.segoy.springboottradingdata.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricalMarketData extends IBKRDataTypeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //Maybe Have to refactor to long
    private Integer id;

    private Integer groupId;

    private Timestamp time;
    private double open;
    private double high;
    private double low;
    private double close;
    private double volume;
    private Integer count;
    private double wap;
}
