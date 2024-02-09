package de.segoy.springboottradingdata.kafkastreams;

import com.ib.client.Types;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.kafkastreams.util.RatioHelper;
import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingdata.model.data.entity.PositionData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StreamOptionsContractDataCombineServiceTest {


    @Mock
    private RatioHelper ratioHelper;
    @Mock
    private PropertiesConfig propertiesConfig;
    @InjectMocks
    private StreamOptionsContractDataCombineService streamOptionsContractDataCombineService;

    private PositionData buildTwo(){
        return PositionData.builder()
                .position(BigDecimal.valueOf(1))
                .account("DU508")
                .averageCost(10)
                .totalCost(10)
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

    private PositionData buildOne(){
        ContractData contractData1 = ContractData.builder()
                .contractId(2)
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
                .averageCost(20)
                .totalCost(40)
                .contractData(contractData1).build();
    }
    private PositionData buildThirdContract(){
        ContractData contractData =ContractData.builder().contractId(3)
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
                .averageCost(-10)
                .totalCost(-30)
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

        PositionData aggregate = streamOptionsContractDataCombineService.combinePositions(positionData2, positionData);

        assertEquals(aggregate, positionData2);
    }
    @Test
    void testWithNewPositionAndNoExistingCombo(){
        when(propertiesConfig.getCOMBO_CONTRACT_ID()).thenReturn(999);
        when(ratioHelper.getRatio(2,1)).thenReturn(new RatioHelper.Ratios(1,2,1));

        PositionData positionDataAgg = buildOne();
        PositionData positionDataRec = buildTwo();

        PositionData aggregate = streamOptionsContractDataCombineService.combinePositions(positionDataRec, positionDataAgg);

        assertEquals(2, aggregate.getContractData().getComboLegs().size());
        assertEquals(999,aggregate.getContractData().getContractId());
        assertEquals(Types.Right.None, aggregate.getContractData().getRight());
        assertEquals("", aggregate.getContractData().getExchange());
        assertNull(aggregate.getContractData().getStrike());
        assertEquals(50, aggregate.getAverageCost());
        assertEquals(Types.SecType.BAG, aggregate.getContractData().getSecurityType());
        assertEquals(BigDecimal.ONE, aggregate.getPosition());
        assertEquals(2, aggregate.getContractData().getComboLegs().get(0).getContractId());
        assertEquals(2, aggregate.getContractData().getComboLegs().get(0).getRatio());
        assertEquals("SMART", aggregate.getContractData().getComboLegs().get(0).getExchange());
        assertEquals(Types.Action.BUY, aggregate.getContractData().getComboLegs().get(0).getAction());

        assertEquals(1, aggregate.getContractData().getComboLegs().get(1).getContractId());
        assertEquals(1, aggregate.getContractData().getComboLegs().get(1).getRatio());
        assertEquals("SMART", aggregate.getContractData().getComboLegs().get(1).getExchange());
        assertEquals(Types.Action.BUY, aggregate.getContractData().getComboLegs().get(1).getAction());
    }

    @Test
    void testWithMultipleAndUpdatedMessages(){
        when(propertiesConfig.getCOMBO_CONTRACT_ID()).thenReturn(999);
        when(ratioHelper.getRatio(1,3)).thenReturn(new RatioHelper.Ratios(1,1,3));
        when(ratioHelper.getRatio(2,1)).thenReturn(new RatioHelper.Ratios(1,2,1));
        when(ratioHelper.getRatio(1,1)).thenReturn(new RatioHelper.Ratios(1,1,1));

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
