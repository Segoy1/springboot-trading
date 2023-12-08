package de.segoy.springboottradingibkr.client.datamodel;

import com.ib.client.TagValue;
import de.segoy.springboottradingibkr.client.datamodel.subtype.Duration;
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

    private Duration backfillDuration;

    // valid: 1/5/10/15/30 secs
    // 1/2/3/5/10/15/20/30 mins
    // 1/2/3/4/8 hours
    // 1 day 2 week 1 month
    private String barSizeSetting;

    private WhatToShowType whatToShow;

    //default Values unlikely to be changed

    private boolean regularTradingHours = true; //for client: 1=true 0=false
    private int dateFormatStyle = 1;
    private boolean keepUpToDate = false;
    private List<TagValue> chartOptions = new ArrayList<>();


}

