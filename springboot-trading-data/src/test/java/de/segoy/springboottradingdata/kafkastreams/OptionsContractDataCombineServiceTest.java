package de.segoy.springboottradingdata.kafkastreams;

import com.ib.client.Types;
import de.segoy.springboottradingdata.kafkastreams.util.RatioHelper;
import de.segoy.springboottradingdata.model.data.entity.ComboLegData;
import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingdata.model.data.entity.PositionData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OptionsContractDataCombineServiceTest {


    @Mock
    private RatioHelper ratioHelper;
    @InjectMocks
    private OptionsContractDataCombineService optionsContractDataCombineService;

    private PositionData buildInitial(){
        return PositionData.builder()
                .position(BigDecimal.valueOf(1))
                .account("DU508")
                .averageCost(10)
                .contractData(
                        ContractData.builder()
                                .contractId(1)
                                .currency("USD")
                                .lastTradeDate("20240216")
                                .right(Types.Right.Call)
                                .multiplier("100")
                                .strike(BigDecimal.valueOf(100))
                                .exchange("SMART")
                                .tradingClass("SPXW").build()
                ).build();
    }

    private PositionData buildAggregatedNew(){
        ContractData contractData1 = ContractData.builder()
                .contractId(2)
                .currency("USD")
                .lastTradeDate("20240216")
                .right(Types.Right.Put)
                .multiplier("100")
                .strike(BigDecimal.valueOf(100))
                .exchange("SMART")
                .tradingClass("SPXW").build();

        return PositionData.builder().position(BigDecimal.valueOf(2))
                .account("DU508")
                .averageCost(20)
                .contractData(contractData1).build();
    }
    private PositionData buildAggreagatedWithCombo(){
        ComboLegData leg1 = ComboLegData.builder()
                .contractId(1)
                .ratio(1)
                .action(Types.Action.BUY)
                .exchange("SMART").build();
        ComboLegData leg2 = ComboLegData.builder()
                .contractId(2)
                .ratio(2)
                .action(Types.Action.BUY)
                .exchange("SMART").build();
        return null;
    }


    @Test
    void combineAggregatedEmptyAndNewTestData() {

        PositionData position = buildInitial();

                PositionData aggregate = optionsContractDataCombineService.combinePositions(position,
                PositionData.builder().build());


        assertEquals(aggregate, position);
    }
    @Test
    void testWithSamePositionUpdatedValues(){

        PositionData positionData = buildInitial();
        PositionData positionData2 = buildInitial();
        positionData2.setPosition(BigDecimal.valueOf(2));

        PositionData aggregate = optionsContractDataCombineService.combinePositions(positionData2, positionData);

        assertEquals(aggregate, positionData2);
    }
    @Test
    void testWithNewPositionAndNoExistingCombo(){
        when(ratioHelper.getRatio(2,1)).thenReturn(new RatioHelper.Ratios(1,2,1));

        PositionData positionDataAgg = buildAggregatedNew();
        PositionData positionDataRec = buildInitial();

        PositionData aggregate = optionsContractDataCombineService.combinePositions(positionDataRec, positionDataAgg);

        assertEquals(2, aggregate.getContractData().getComboLegs().size());
    }
}
