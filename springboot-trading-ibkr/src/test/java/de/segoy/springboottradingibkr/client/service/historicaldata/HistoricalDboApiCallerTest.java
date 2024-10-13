package de.segoy.springboottradingibkr.client.service.historicaldata;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.TagValue;
import de.segoy.springboottradingdata.model.data.HistoricalDataSettings;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.subtype.BarSizeSetting;
import de.segoy.springboottradingdata.model.subtype.WhatToShowType;
import de.segoy.springboottradingdata.modelconverter.ContractDboToIBKRContract;
import de.segoy.springboottradingdata.service.IBKRTimeStampFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoricalDboApiCallerTest {

    @Mock
    private EClientSocket client;
    @Mock
    private ContractDboToIBKRContract contractDboToIBKRContract;
    @Mock
    private IBKRTimeStampFormatter ibkrTimeStampFormatter;
    @InjectMocks
    private HistoricalDataApiCaller historicalDataApiCaller;

    @Test
    void testApiCall(){
        ContractDbo contractDBO = ContractDbo.builder().contractId(1).build();
        Contract contract = new Contract();
        HistoricalDataSettings settings = HistoricalDataSettings.builder()
                .contractDBO(contractDBO)
                .backfillDuration("1 Y")
                .backfillEndTime(Timestamp.from(Instant.now()))
                .barSizeSetting(BarSizeSetting.ONE_DAY)
                .whatToShow(WhatToShowType.TRADES)
                .regularTradingHours(true)
                .dateFormatStyle(2)
                .keepUpToDate(true)
                .chartOptions(List.of(new TagValue())).build();

        when(contractDboToIBKRContract.convertContractData(settings.getContractDBO())).thenReturn(contract);
        when(ibkrTimeStampFormatter.formatTimestampToDateAndTime(settings.getBackfillEndTime())).thenReturn(
                "formattedDT");


        historicalDataApiCaller.callApi(settings);

        verify(client, times(1))
                .reqHistoricalData(1, contract,"formattedDT",
                        "1 Y","1 day","TRADES",
                        1,2,true,settings.getChartOptions());
        verify(contractDboToIBKRContract, times(1)).convertContractData(contractDBO);
        verify(ibkrTimeStampFormatter, times(1)).formatTimestampToDateAndTime(settings.getBackfillEndTime());
    }

}
