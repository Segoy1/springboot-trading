package de.segoy.springboottradingdata.kafkastreams;

import com.ib.client.Types;
import de.segoy.springboottradingdata.kafkastreams.util.RatioHelper;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.entity.PositionDbo;
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
class StreamOptionsContractDboCombineServiceTest {


    @Mock
    private RatioHelper ratioHelper;
    @InjectMocks
    private StreamOptionsContractDataCombineService streamOptionsContractDataCombineService;

    private PositionDbo buildTwo(){
        return PositionDbo.builder()
                .position(BigDecimal.valueOf(1))
                .account("DU508")
                .averageCost(10)
                .totalCost(10)
                .contractDBO(
                        ContractDbo.builder()
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

    private PositionDbo buildOne(){
        ContractDbo contractDbo1 = ContractDbo.builder()
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

        return PositionDbo.builder().position(BigDecimal.valueOf(2))
                .account("DU508")
                .averageCost(20)
                .totalCost(40)
                .contractDBO(contractDbo1).build();
    }
    private PositionDbo buildThirdContract(){
        ContractDbo contractDBO = ContractDbo.builder().contractId(13)
               .right(Types.Right.Put)
               .currency("USD")
               .exchange("SMART")
               .lastTradeDate("20240216")
               .multiplier("100")
               .tradingClass("SPXW")
               .strike(BigDecimal.valueOf(90))
               .securityType(Types.SecType.OPT).build();

        return PositionDbo.builder()
                .position(BigDecimal.valueOf(-3))
                .averageCost(-10)
                .totalCost(-30)
                .account("DU508")
                .contractDBO(contractDBO).build();


    }


    @Test
    void combineAggregatedEmptyAndNewTestData() {

        PositionDbo position = buildTwo();

                PositionDbo aggregate = streamOptionsContractDataCombineService.combinePositions(position,
                PositionDbo.builder().build());


        assertEquals(aggregate, position);
    }
    @Test
    void testWithSamePositionUpdatedValues(){

        PositionDbo positionDBO = buildTwo();
        PositionDbo positionDbo2 = buildTwo();
        positionDbo2.setPosition(BigDecimal.valueOf(2));

        PositionDbo aggregate = streamOptionsContractDataCombineService.combinePositions(positionDbo2,
                positionDBO);

        assertEquals(aggregate, positionDbo2);
    }
    @Test
    void testWithNewPositionAndNoExistingCombo(){
        when(ratioHelper.getRatio(2,1)).thenReturn(new RatioHelper.Ratios(1,2,1));

        PositionDbo positionDboAgg = buildOne();
        PositionDbo positionDboRec = buildTwo();

        PositionDbo aggregate = streamOptionsContractDataCombineService.combinePositions(positionDboRec,
                positionDboAgg);

        assertEquals(2, aggregate.getContractDBO().getComboLegs().size());
        assertEquals(23,aggregate.getContractDBO().getContractId());
        assertEquals(Types.Right.None, aggregate.getContractDBO().getRight());
        assertEquals("SMART", aggregate.getContractDBO().getExchange());
        assertNull(aggregate.getContractDBO().getStrike());
        assertEquals(50, aggregate.getAverageCost());
        assertEquals(Types.SecType.BAG, aggregate.getContractDBO().getSecurityType());
        assertEquals(BigDecimal.ONE, aggregate.getPosition());
        assertEquals(12, aggregate.getContractDBO().getComboLegs().get(0).getContractId());
        assertEquals(2, aggregate.getContractDBO().getComboLegs().get(0).getRatio());
        assertEquals("SMART", aggregate.getContractDBO().getComboLegs().get(0).getExchange());
        assertEquals(Types.Action.BUY, aggregate.getContractDBO().getComboLegs().get(0).getAction());

        assertEquals(11, aggregate.getContractDBO().getComboLegs().get(1).getContractId());
        assertEquals(1, aggregate.getContractDBO().getComboLegs().get(1).getRatio());
        assertEquals("SMART", aggregate.getContractDBO().getComboLegs().get(1).getExchange());
        assertEquals(Types.Action.BUY, aggregate.getContractDBO().getComboLegs().get(1).getAction());
    }

    @Test
    void testWithMultipleAndUpdatedMessages(){
        when(ratioHelper.getRatio(1,3)).thenReturn(new RatioHelper.Ratios(1,1,3));
        when(ratioHelper.getRatio(2,1)).thenReturn(new RatioHelper.Ratios(1,2,1));
        when(ratioHelper.getRatio(1,1)).thenReturn(new RatioHelper.Ratios(1,1,1));

        PositionDbo positionDbo1 = buildOne();
        PositionDbo positionDbo2 = buildTwo();
        PositionDbo positionDbo3 = buildThirdContract();

        PositionDbo aggregate1 = streamOptionsContractDataCombineService.combinePositions(positionDbo3,
                streamOptionsContractDataCombineService.combinePositions(positionDbo2,
                        positionDbo1));

        assertEquals(20,aggregate1.getAverageCost());


        PositionDbo aggregate2 =
                streamOptionsContractDataCombineService.combinePositions(positionDbo1,
                streamOptionsContractDataCombineService.combinePositions(positionDbo3,
                        streamOptionsContractDataCombineService.combinePositions(positionDbo1,
                        streamOptionsContractDataCombineService.combinePositions(positionDbo2, aggregate1))));


        assertEquals(3, aggregate2.getContractDBO().getComboLegs().size());
        assertEquals(20, aggregate2.getAverageCost());

    }
}
