package de.segoy.springboottradingdata.kafkastreams;

import com.ib.client.Types;
import de.segoy.springboottradingdata.kafkastreams.util.RatioHelper;
import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.model.data.entity.PositionDataDBO;
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
class StreamOptionsContractDataDBOCombineServiceTest {


    @Mock
    private RatioHelper ratioHelper;
    @InjectMocks
    private StreamOptionsContractDataCombineService streamOptionsContractDataCombineService;

    private PositionDataDBO buildTwo(){
        return PositionDataDBO.builder()
                .position(BigDecimal.valueOf(1))
                .account("DU508")
                .averageCost(10)
                .totalCost(10)
                .contractDataDBO(
                        ContractDataDBO.builder()
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

    private PositionDataDBO buildOne(){
        ContractDataDBO contractDataDBO1 = ContractDataDBO.builder()
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

        return PositionDataDBO.builder().position(BigDecimal.valueOf(2))
                .account("DU508")
                .averageCost(20)
                .totalCost(40)
                .contractDataDBO(contractDataDBO1).build();
    }
    private PositionDataDBO buildThirdContract(){
        ContractDataDBO contractDataDBO = ContractDataDBO.builder().contractId(13)
               .right(Types.Right.Put)
               .currency("USD")
               .exchange("SMART")
               .lastTradeDate("20240216")
               .multiplier("100")
               .tradingClass("SPXW")
               .strike(BigDecimal.valueOf(90))
               .securityType(Types.SecType.OPT).build();

        return PositionDataDBO.builder()
                .position(BigDecimal.valueOf(-3))
                .averageCost(-10)
                .totalCost(-30)
                .account("DU508")
                .contractDataDBO(contractDataDBO).build();


    }


    @Test
    void combineAggregatedEmptyAndNewTestData() {

        PositionDataDBO position = buildTwo();

                PositionDataDBO aggregate = streamOptionsContractDataCombineService.combinePositions(position,
                PositionDataDBO.builder().build());


        assertEquals(aggregate, position);
    }
    @Test
    void testWithSamePositionUpdatedValues(){

        PositionDataDBO positionDataDBO = buildTwo();
        PositionDataDBO positionDataDBO2 = buildTwo();
        positionDataDBO2.setPosition(BigDecimal.valueOf(2));

        PositionDataDBO aggregate = streamOptionsContractDataCombineService.combinePositions(positionDataDBO2,
                positionDataDBO);

        assertEquals(aggregate, positionDataDBO2);
    }
    @Test
    void testWithNewPositionAndNoExistingCombo(){
        when(ratioHelper.getRatio(2,1)).thenReturn(new RatioHelper.Ratios(1,2,1));

        PositionDataDBO positionDataDBOAgg = buildOne();
        PositionDataDBO positionDataDBORec = buildTwo();

        PositionDataDBO aggregate = streamOptionsContractDataCombineService.combinePositions(positionDataDBORec,
                positionDataDBOAgg);

        assertEquals(2, aggregate.getContractDataDBO().getComboLegs().size());
        assertEquals(23,aggregate.getContractDataDBO().getContractId());
        assertEquals(Types.Right.None, aggregate.getContractDataDBO().getRight());
        assertEquals("SMART", aggregate.getContractDataDBO().getExchange());
        assertNull(aggregate.getContractDataDBO().getStrike());
        assertEquals(50, aggregate.getAverageCost());
        assertEquals(Types.SecType.BAG, aggregate.getContractDataDBO().getSecurityType());
        assertEquals(BigDecimal.ONE, aggregate.getPosition());
        assertEquals(12, aggregate.getContractDataDBO().getComboLegs().get(0).getContractId());
        assertEquals(2, aggregate.getContractDataDBO().getComboLegs().get(0).getRatio());
        assertEquals("SMART", aggregate.getContractDataDBO().getComboLegs().get(0).getExchange());
        assertEquals(Types.Action.BUY, aggregate.getContractDataDBO().getComboLegs().get(0).getAction());

        assertEquals(11, aggregate.getContractDataDBO().getComboLegs().get(1).getContractId());
        assertEquals(1, aggregate.getContractDataDBO().getComboLegs().get(1).getRatio());
        assertEquals("SMART", aggregate.getContractDataDBO().getComboLegs().get(1).getExchange());
        assertEquals(Types.Action.BUY, aggregate.getContractDataDBO().getComboLegs().get(1).getAction());
    }

    @Test
    void testWithMultipleAndUpdatedMessages(){
        when(ratioHelper.getRatio(1,3)).thenReturn(new RatioHelper.Ratios(1,1,3));
        when(ratioHelper.getRatio(2,1)).thenReturn(new RatioHelper.Ratios(1,2,1));
        when(ratioHelper.getRatio(1,1)).thenReturn(new RatioHelper.Ratios(1,1,1));

        PositionDataDBO positionDataDBO1 = buildOne();
        PositionDataDBO positionDataDBO2 = buildTwo();
        PositionDataDBO positionDataDBO3 = buildThirdContract();

        PositionDataDBO aggregate1 = streamOptionsContractDataCombineService.combinePositions(positionDataDBO3,
                streamOptionsContractDataCombineService.combinePositions(positionDataDBO2,
                        positionDataDBO1));

        assertEquals(20,aggregate1.getAverageCost());


        PositionDataDBO aggregate2 =
                streamOptionsContractDataCombineService.combinePositions(positionDataDBO1,
                streamOptionsContractDataCombineService.combinePositions(positionDataDBO3,
                        streamOptionsContractDataCombineService.combinePositions(positionDataDBO1,
                        streamOptionsContractDataCombineService.combinePositions(positionDataDBO2, aggregate1))));


        assertEquals(3, aggregate2.getContractDataDBO().getComboLegs().size());
        assertEquals(20, aggregate2.getAverageCost());

    }
}
