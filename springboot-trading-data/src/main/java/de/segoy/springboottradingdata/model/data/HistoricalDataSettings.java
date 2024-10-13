package de.segoy.springboottradingdata.model.data;

import com.ib.client.TagValue;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.subtype.BarSizeSetting;
import de.segoy.springboottradingdata.model.subtype.WhatToShowType;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper Class to get Historical Data Parameter right
 * Documentaion: <a href="https://interactivebrokers.github.io/tws-api/historical_bars.html">...</a>
 * Author: Segoy
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoricalDataSettings extends IBKRDataType {

    private ContractDbo contractDBO;

    //End Date of historical Data
    private Timestamp backfillEndTime;

    //Duration of Historical DAta
    private String backfillDuration;

    //Time Frame of each Bar
    private BarSizeSetting barSizeSetting;

    //e.G. Trades, Ask, Bid, BID_ASK ...
    private WhatToShowType whatToShow;

    //2 gives linux Timestamp 1 gives yyyyMMdd
    private int dateFormatStyle;//default 1
    //default Values unlikely to be changed


    private boolean regularTradingHours; // default= true, for client: 1=true 0=false
    private boolean keepUpToDate;//default false;
    private List<TagValue> chartOptions = new ArrayList<>();


}

