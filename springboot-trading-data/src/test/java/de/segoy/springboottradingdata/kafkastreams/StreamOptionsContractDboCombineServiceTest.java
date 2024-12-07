package de.segoy.springboottradingdata.kafkastreams;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.ib.client.Types;
import de.segoy.springboottradingdata.kafkastreams.util.RatioHelper;
import de.segoy.springboottradingdata.model.data.kafka.ContractData;
import de.segoy.springboottradingdata.model.data.kafka.PositionData;
import de.segoy.springboottradingdata.service.StrategyComboLegsDescriptionCreator;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StreamOptionsContractDataCombineServiceTest {


    @Mock
    private RatioHelper ratioHelper;
    @Mock
    private StrategyComboLegsDescriptionCreator strategyComboLegsDescriptionCreator;
    @InjectMocks
    private StreamOptionsContractDataCombineService streamOptionsContractDataCombineService;

    private PositionData buildTwo(){
        return PositionData.builder()
                .position(BigDecimal.valueOf(1))
                .account("DU508")
                .averageCost(BigDecimal.TEN)
                .totalCost(BigDecimal.TEN)
                .contractData(
                        ContractData.builder()
                                .contractId(11)
                                .currency("USD")
                                .lastTradeDate("20240216")
                                .right(Types.Right.Call)
                                .multiplier("100")
                                .strike(BigDecimal.valueOf(100))
                                .exchange("SMART")
                                .tradingClass("SPXW").build()
                ).build();
    }

    private PositionData buildOne(){
        ContractData contractData1 = ContractData.builder()
                .contractId(12)
                .currency("USD")
                .lastTradeDate("20240216")
                .right(Types.Right.Put)
                .multiplier("100")
                .strike(BigDecimal.valueOf(100))
                .exchange("SMART")
                .securityType(Types.SecType.OPT)
                .tradingClass("SPXW")
                .build();

        return PositionData.builder().position(BigDecimal.valueOf(2))
                .account("DU508")
                .averageCost(BigDecimal.valueOf(20))
                .totalCost(BigDecimal.valueOf(40))
                .contractData(contractData1).build();
    }
    private PositionData buildThirdContract(){
        ContractData contractData = ContractData.builder().contractId(13)
               .right(Types.Right.Put)
               .currency("USD")
               .exchange("SMART")
               .lastTradeDate("20240216")
               .multiplier("100")
               .tradingClass("SPXW")
               .strike(BigDecimal.valueOf(90))
               .securityType(Types.SecType.OPT).build();

        return PositionData.builder()
                .position(BigDecimal.valueOf(-3))
                .averageCost(BigDecimal.valueOf(-10))
                .totalCost(BigDecimal.valueOf(-30))
                .account("DU508")
                .contractData(contractData).build();


    }


    @Test
    void combineAggregatedEmptyAndNewTestData() {

        PositionData position = buildTwo();

                PositionData aggregate = streamOptionsContractDataCombineService.combinePositions(position,
                PositionData.builder().build());


        assertEquals(aggregate, position);
    }
    @Test
    void testWithSamePositionUpdatedValues(){

        PositionData positionData = buildTwo();
        PositionData positionData2 = buildTwo();
        positionData2.setPosition(BigDecimal.valueOf(2));

        PositionData aggregate = streamOptionsContractDataCombineService.combinePositions(positionData2,
                positionData);

        assertEquals(aggregate, positionData2);
    }
    @Test
    void testWithNewPositionAndNoExistingCombo(){
        when(ratioHelper.getRatio(2,1)).thenReturn(new RatioHelper.Ratios(1,2,1));
        when(strategyComboLegsDescriptionCreator.generateComboLegsDescription(any())).thenReturn("");

        PositionData positionDataAgg = buildOne();
        PositionData positionDataRec = buildTwo();

        PositionData aggregate = streamOptionsContractDataCombineService.combinePositions(positionDataRec,
                positionDataAgg);

        assertEquals(2, aggregate.getContractData().getComboLegs().size());
        assertEquals(23,aggregate.getContractData().getContractId());
        assertEquals(Types.Right.None, aggregate.getContractData().getRight());
        assertEquals("SMART", aggregate.getContractData().getExchange());
        assertNull(aggregate.getContractData().getStrike());
        assertEquals(50, aggregate.getAverageCost());
        assertEquals(Types.SecType.BAG, aggregate.getContractData().getSecurityType());
        assertEquals(BigDecimal.ONE, aggregate.getPosition());
        assertEquals(12, aggregate.getContractData().getComboLegs().get(0).getContractId());
        assertEquals(2, aggregate.getContractData().getComboLegs().get(0).getRatio());
        assertEquals("SMART", aggregate.getContractData().getComboLegs().get(0).getExchange());
        assertEquals(Types.Action.BUY, aggregate.getContractData().getComboLegs().get(0).getAction());

        assertEquals(11, aggregate.getContractData().getComboLegs().get(1).getContractId());
        assertEquals(1, aggregate.getContractData().getComboLegs().get(1).getRatio());
        assertEquals("SMART", aggregate.getContractData().getComboLegs().get(1).getExchange());
        assertEquals(Types.Action.BUY, aggregate.getContractData().getComboLegs().get(1).getAction());
    }

    @Test
    void testWithMultipleAndUpdatedMessages(){
        when(ratioHelper.getRatio(1,3)).thenReturn(new RatioHelper.Ratios(1,1,3));
        when(ratioHelper.getRatio(2,1)).thenReturn(new RatioHelper.Ratios(1,2,1));
        when(ratioHelper.getRatio(1,1)).thenReturn(new RatioHelper.Ratios(1,1,1));
        when(strategyComboLegsDescriptionCreator.generateComboLegsDescription(any())).thenReturn("");

        PositionData positionData1 = buildOne();
        PositionData positionData2 = buildTwo();
        PositionData positionData3 = buildThirdContract();

        PositionData aggregate1 = streamOptionsContractDataCombineService.combinePositions(positionData3,
                streamOptionsContractDataCombineService.combinePositions(positionData2,
                        positionData1));

        assertEquals(20,aggregate1.getAverageCost());


        PositionData aggregate2 =
                streamOptionsContractDataCombineService.combinePositions(positionData1,
                streamOptionsContractDataCombineService.combinePositions(positionData3,
                        streamOptionsContractDataCombineService.combinePositions(positionData1,
                        streamOptionsContractDataCombineService.combinePositions(positionData2, aggregate1))));


        assertEquals(3, aggregate2.getContractData().getComboLegs().size());
        assertEquals(20, aggregate2.getAverageCost());

    }
}
