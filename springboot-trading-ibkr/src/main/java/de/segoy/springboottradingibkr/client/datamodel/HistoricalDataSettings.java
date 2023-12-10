package de.segoy.springboottradingibkr.client.datamodel;

import com.ib.client.TagValue;
import de.segoy.springboottradingibkr.client.datamodel.subtype.BarSizeSetting;
import de.segoy.springboottradingibkr.client.datamodel.subtype.WhatToShowType;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper Class to get Historical Data Parameter right
 * Documentaion: <a href="https://interactivebrokers.github.io/tws-api/historical_bars.html">...</a>
 * <p>
 * Author: Segoy
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoricalDataSettings {

    private Timestamp backfillEndTime;

    private String backfillDuration;

    private BarSizeSetting barSizeSetting;

    private WhatToShowType whatToShow;

    //default Values unlikely to be changed


    private boolean regularTradingHours; // default= true, for client: 1=true 0=false
    private int dateFormatStyle;//default 1
    private boolean keepUpToDate;//default false;
    private List<TagValue> chartOptions = new ArrayList<>();


}

