package de.segoy.springboottradingdata.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.segoy.springboottradingdata.constants.AutoDayTradeConstants;
import de.segoy.springboottradingdata.kafkastreams.StreamOptionChainDataCreator;
import de.segoy.springboottradingdata.kafkastreams.StreamOptionsContractDataCombineService;
import de.segoy.springboottradingdata.kafkastreams.util.RatioHelper;
import de.segoy.springboottradingdata.model.data.OptionChainData;
import de.segoy.springboottradingdata.model.data.OptionListData;
import de.segoy.springboottradingdata.model.data.OptionMarketData;
import de.segoy.springboottradingdata.model.data.entity.ComboLegData;
import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingdata.model.data.entity.PositionData;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import de.segoy.springboottradingdata.optionstradingservice.OptionTickerIdResolver;
import de.segoy.springboottradingdata.service.IBKRTimeStampFormatter;
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

    PositionData p1 =
        PositionData.builder()
            .contractData(
                ContractData.builder()
                    .symbol(Symbol.SPX)
                    .lastTradeDate("20241004")
                    .tradingClass("OPT")
                    .contractId(1)
                    .build())
            .build();
    PositionData p2 =
        PositionData.builder()
            .contractData(
                ContractData.builder()
                    .symbol(Symbol.SPX)
                    .lastTradeDate("20241004")
                    .tradingClass("OPT")
                    .contractId(2)
                    .build())
            .build();
    PositionData p3 =
            PositionData.builder()
                    .contractData(
                            ContractData.builder()
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
    TestInputTopic<String, PositionData> inputTopic = getInputPositionTopic();
    TestOutputTopic<String, PositionData> outputTopic = getOutputPositionTopic();
    List<PositionData> positions = List.of(p1, p2, p3);
    positions.forEach(
        position ->
            inputTopic.pipeInput(position.getContractData().getContractId() + "", position));

    long queue = outputTopic.getQueueSize();
    List<PositionData> actual = outputTopic.readValuesToList();
    assertThat(queue).isEqualTo(3);
    assertThat(actual).isNotNull();
    assertThat(actual).hasSize(3);
    assertThat(actual.get(2).getContractData().getComboLegs()).hasSize(3);
  }

  @Test
  void testMarketData() {

    when(kafkaConstantsConfig.getOPTION_MARKET_DATA_TOPIC()).thenReturn(IBKR_OPTION_MARKET_DATA);
    when(kafkaConstantsConfig.getOPTION_CHAIN_DATA_TOPIC()).thenReturn(IBKR_OPTION_CHAIN);
    when(kafkaConstantsConfig.getOPTION_MARKET_DATA_AGGREGATE_TOPIC()).thenReturn("aggregate");

    Topology topology = streamsConfig.processOptionsMarketDataForStrategy(new StreamsBuilder());

    testDriver = new TopologyTestDriver(topology, props);
    TestInputTopic<String, OptionMarketData> inputTopic = getInputMarketDataTopic();
    TestOutputTopic<String, OptionChainData> outputTopic = getOutputMarketDataTopic();

    List<OptionMarketData> options = getOptionMarketData();
    options.forEach(
        option ->
            inputTopic.pipeInput(
                option.getSymbol()
                    + AutoDayTradeConstants.DELIMITER
                    + option.getLastTradeDate()
                    + AutoDayTradeConstants.DELIMITER
                    + option.getStrike(),
                option));

    List<OptionChainData> actual = outputTopic.readValuesToList();
    assertThat(actual).isNotNull().hasSize(4);
    assertThat(actual.get(3).getCalls().get(1).getStrike()).isEqualTo(1);
    assertThat(actual.get(3).getCalls().get(2).getStrike()).isEqualTo(2);
  }

  private static List<OptionMarketData> getOptionMarketData() {
    OptionMarketData opt1 =
        OptionMarketData.builder().strike(1).symbol(Symbol.SPX).lastTradeDate("20241004").build();
    OptionMarketData opt2 =
        OptionMarketData.builder().strike(2).symbol(Symbol.SPX).lastTradeDate("20241004").build();
    OptionMarketData opt3 =
        OptionMarketData.builder().strike(3).symbol(Symbol.SPX).lastTradeDate("20241004").build();
    OptionMarketData opt4 =
        OptionMarketData.builder().strike(4).symbol(Symbol.SPX).lastTradeDate("20241004").build();
    List<OptionMarketData> options = List.of(opt1, opt2, opt3, opt4);
    return options;
  }

  private TestOutputTopic<String, PositionData> getOutputPositionTopic() {
    ObjectMapper mapper = new ObjectMapper();
    Serde<PositionData> positionSerde = new JsonSerde<>(PositionData.class, mapper);
    return testDriver.createOutputTopic(
        IBKR_POSITIONS, Serdes.String().deserializer(), positionSerde.deserializer());
  }

  private TestInputTopic<String, PositionData> getInputPositionTopic() {
    ObjectMapper mapper = new ObjectMapper();
    Serde<PositionData> positionSerde = new JsonSerde<>(PositionData.class, mapper);
    return testDriver.createInputTopic(
        IBKR_OPTION_POSITIONS, Serdes.String().serializer(), positionSerde.serializer());
  }

  private TestOutputTopic<String, OptionChainData> getOutputMarketDataTopic() {
    ObjectMapper mapper = new ObjectMapper();
    Serde<OptionChainData> optionChainDataSerde = new JsonSerde<>(OptionChainData.class, mapper);
    return testDriver.createOutputTopic(
        IBKR_OPTION_CHAIN, Serdes.String().deserializer(), optionChainDataSerde.deserializer());
  }

  private TestInputTopic<String, OptionMarketData> getInputMarketDataTopic() {
    ObjectMapper mapper = new ObjectMapper();
    Serde<OptionMarketData> optionMarketDataSerde = new JsonSerde<>(OptionMarketData.class, mapper);
    return testDriver.createInputTopic(
        IBKR_OPTION_MARKET_DATA, Serdes.String().serializer(), optionMarketDataSerde.serializer());
  }

  private StreamOptionChainDataCreator mockStreamOptionChainDataCreator() {
    return new StreamOptionChainDataCreator(
        new OptionTickerIdResolver(new IBKRTimeStampFormatter(new PropertiesConfig()))) {
      @Override
      public OptionChainData buildChain(
          OptionMarketData marketData, OptionChainData aggregatedChain) {
        if (aggregatedChain.getCalls() == null) {
          aggregatedChain.setCalls(new OptionListData());
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
      public PositionData combinePositions(
          PositionData receivedPosition, PositionData aggregatedPosition) {
        if (aggregatedPosition.getContractData() == null) {
          aggregatedPosition.setContractData(
              ContractData.builder()
                  .symbol(receivedPosition.getContractData().getSymbol())
                  .comboLegs(new ArrayList<>())
                  .build());
        }
        aggregatedPosition
            .getContractData()
            .getComboLegs()
            .add(
                ComboLegData.builder()
                    .contractId(receivedPosition.getContractData().getContractId())
                    .build());
        return aggregatedPosition;
      }
    };
  }
}
