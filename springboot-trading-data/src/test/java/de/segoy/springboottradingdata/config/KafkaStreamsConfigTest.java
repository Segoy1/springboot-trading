package de.segoy.springboottradingdata.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.segoy.springboottradingdata.constants.AutoDayTradeConstants;
import de.segoy.springboottradingdata.kafkastreams.StreamOptionChainDataCreator;
import de.segoy.springboottradingdata.kafkastreams.StreamOptionsContractDataCombineService;
import de.segoy.springboottradingdata.kafkastreams.util.RatioHelper;
import de.segoy.springboottradingdata.model.data.entity.ComboLegDbo;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.entity.PositionDbo;
import de.segoy.springboottradingdata.model.data.kafka.KafkaOptionChainData;
import de.segoy.springboottradingdata.model.data.kafka.KafkaOptionListData;
import de.segoy.springboottradingdata.model.data.kafka.KafkaOptionMarketData;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.serializer.JsonSerde;

@ExtendWith(MockitoExtension.class)
class KafkaStreamsConfigTest {

  private static final String IBKR_OPTION_MARKET_DATA = "ibkr_option_market_data";
  private static final String IBKR_OPTION_CHAIN = "ibkr_option_chain";
  public static final String IBKR_POSITIONS = "ibkr_positions";
  public static final String IBKR_OPTION_POSITIONS = "ibkr_option_positions";

  @Mock private KafkaConstantsConfig kafkaConstantsConfig;
  @Mock private StreamOptionsContractDataCombineService dataCombineService;

  private KafkaStreamsConfig streamsConfig;

  Properties props;
  TopologyTestDriver testDriver;

  @BeforeEach
  void setUp() {
    props = new Properties();
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "test");
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class);

    streamsConfig =
        new KafkaStreamsConfig(
            kafkaConstantsConfig,
            mockStreamOptionChainDataCombineService(),
            mockStreamOptionChainDataCreator());
  }

  @AfterEach
  void tearDown() {
    testDriver.close();
  }

  @Test
  void testPositionData() {

    PositionDbo p1 =
        PositionDbo.builder()
            .contractDBO(
                ContractDbo.builder()
                    .symbol(Symbol.SPX)
                    .lastTradeDate("20241004")
                    .tradingClass("OPT")
                    .contractId(1)
                    .build())
            .build();
    PositionDbo p2 =
        PositionDbo.builder()
            .contractDBO(
                ContractDbo.builder()
                    .symbol(Symbol.SPX)
                    .lastTradeDate("20241004")
                    .tradingClass("OPT")
                    .contractId(2)
                    .build())
            .build();
    PositionDbo p3 =
            PositionDbo.builder()
                    .contractDBO(
                            ContractDbo.builder()
                                    .symbol(Symbol.SPX)
                                    .lastTradeDate("20241004")
                                    .tradingClass("OPT")
                                    .contractId(3)
                                    .build())
                    .build();

    when(kafkaConstantsConfig.getOPTION_POSITIONS_TOPIC()).thenReturn(IBKR_OPTION_POSITIONS);
    when(kafkaConstantsConfig.getPOSITION_TOPIC()).thenReturn(IBKR_POSITIONS);
    when(kafkaConstantsConfig.getPOSITIONS_AGGREGATE_TOPIC()).thenReturn("aggregate");

    Topology topology = streamsConfig.processOptionsContractData(new StreamsBuilder());

    testDriver = new TopologyTestDriver(topology, props);
    TestInputTopic<String, PositionDbo> inputTopic = getInputPositionTopic();
    TestOutputTopic<String, PositionDbo> outputTopic = getOutputPositionTopic();
    List<PositionDbo> positions = List.of(p1, p2, p3);
    positions.forEach(
        position ->
            inputTopic.pipeInput(position.getContractDBO().getContractId() + "", position));

    long queue = outputTopic.getQueueSize();
    List<PositionDbo> actual = outputTopic.readValuesToList();
    assertThat(queue).isEqualTo(3);
    assertThat(actual).isNotNull();
    assertThat(actual).hasSize(3);
    assertThat(actual.get(2).getContractDBO().getComboLegs()).hasSize(3);
  }

  @Test
  void testMarketData() {

    when(kafkaConstantsConfig.getOPTION_MARKET_DATA_TOPIC()).thenReturn(IBKR_OPTION_MARKET_DATA);
    when(kafkaConstantsConfig.getOPTION_CHAIN_DATA_TOPIC()).thenReturn(IBKR_OPTION_CHAIN);
    when(kafkaConstantsConfig.getOPTION_MARKET_DATA_AGGREGATE_TOPIC()).thenReturn("aggregate");

    Topology topology = streamsConfig.processOptionsMarketDataForStrategy(new StreamsBuilder());

    testDriver = new TopologyTestDriver(topology, props);
    TestInputTopic<String, KafkaOptionMarketData> inputTopic = getInputMarketDataTopic();
    TestOutputTopic<String, KafkaOptionChainData> outputTopic = getOutputMarketDataTopic();

    List<KafkaOptionMarketData> options = getOptionMarketData();
    options.forEach(
        option ->
            inputTopic.pipeInput(
                option.getSymbol()
                    + AutoDayTradeConstants.DELIMITER
                    + option.getLastTradeDate()
                    + AutoDayTradeConstants.DELIMITER
                    + option.getStrike(),
                option));

    List<KafkaOptionChainData> actual = outputTopic.readValuesToList();
    assertThat(actual).isNotNull().hasSize(4);
    assertThat(actual.get(3).getCalls().get(1).getStrike()).isEqualTo(1);
    assertThat(actual.get(3).getCalls().get(2).getStrike()).isEqualTo(2);
  }

  private static List<KafkaOptionMarketData> getOptionMarketData() {
    KafkaOptionMarketData opt1 =
        KafkaOptionMarketData.builder().strike(1).symbol(Symbol.SPX).lastTradeDate("20241004").build();
    KafkaOptionMarketData opt2 =
        KafkaOptionMarketData.builder().strike(2).symbol(Symbol.SPX).lastTradeDate("20241004").build();
    KafkaOptionMarketData opt3 =
        KafkaOptionMarketData.builder().strike(3).symbol(Symbol.SPX).lastTradeDate("20241004").build();
    KafkaOptionMarketData opt4 =
        KafkaOptionMarketData.builder().strike(4).symbol(Symbol.SPX).lastTradeDate("20241004").build();
    List<KafkaOptionMarketData> options = List.of(opt1, opt2, opt3, opt4);
    return options;
  }

  private TestOutputTopic<String, PositionDbo> getOutputPositionTopic() {
    ObjectMapper mapper = new ObjectMapper();
    Serde<PositionDbo> positionSerde = new JsonSerde<>(PositionDbo.class, mapper);
    return testDriver.createOutputTopic(
        IBKR_POSITIONS, Serdes.String().deserializer(), positionSerde.deserializer());
  }

  private TestInputTopic<String, PositionDbo> getInputPositionTopic() {
    ObjectMapper mapper = new ObjectMapper();
    Serde<PositionDbo> positionSerde = new JsonSerde<>(PositionDbo.class, mapper);
    return testDriver.createInputTopic(
        IBKR_OPTION_POSITIONS, Serdes.String().serializer(), positionSerde.serializer());
  }

  private TestOutputTopic<String, KafkaOptionChainData> getOutputMarketDataTopic() {
    ObjectMapper mapper = new ObjectMapper();
    Serde<KafkaOptionChainData> optionChainDataSerde = new JsonSerde<>(KafkaOptionChainData.class, mapper);
    return testDriver.createOutputTopic(
        IBKR_OPTION_CHAIN, Serdes.String().deserializer(), optionChainDataSerde.deserializer());
  }

  private TestInputTopic<String, KafkaOptionMarketData> getInputMarketDataTopic() {
    ObjectMapper mapper = new ObjectMapper();
    Serde<KafkaOptionMarketData> optionMarketDataSerde = new JsonSerde<>(KafkaOptionMarketData.class, mapper);
    return testDriver.createInputTopic(
        IBKR_OPTION_MARKET_DATA, Serdes.String().serializer(), optionMarketDataSerde.serializer());
  }

  private StreamOptionChainDataCreator mockStreamOptionChainDataCreator() {
    return new StreamOptionChainDataCreator() {
      @Override
      public KafkaOptionChainData buildChain(
              KafkaOptionMarketData marketData, KafkaOptionChainData aggregatedChain) {
        if (aggregatedChain.getCalls() == null) {
          aggregatedChain.setCalls(new KafkaOptionListData());
          aggregatedChain.setSymbol(marketData.getSymbol());
        }
        aggregatedChain.getCalls().put(marketData.getStrike(), marketData);
        return aggregatedChain;
      }
    };
  }

  private StreamOptionsContractDataCombineService mockStreamOptionChainDataCombineService() {
    return new StreamOptionsContractDataCombineService(new RatioHelper()) {
      @Override
      public PositionDbo combinePositions(
              PositionDbo receivedPosition, PositionDbo aggregatedPosition) {
        if (aggregatedPosition.getContractDBO() == null) {
          aggregatedPosition.setContractDBO(
              ContractDbo.builder()
                  .symbol(receivedPosition.getContractDBO().getSymbol())
                  .comboLegs(new ArrayList<>())
                  .build());
        }
        aggregatedPosition
            .getContractDBO()
            .getComboLegs()
            .add(
                ComboLegDbo.builder()
                    .contractId(receivedPosition.getContractDBO().getContractId())
                    .build());
        return aggregatedPosition;
      }
    };
  }
}
