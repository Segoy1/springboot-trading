package de.segoy.springboottradingweb.spxautotrade;

import com.ib.client.TickType;
import de.segoy.springboottradingdata.config.TradeRuleSettingsConfig;
import de.segoy.springboottradingdata.model.data.kafka.OptionChainData;
import de.segoy.springboottradingdata.model.data.kafka.StandardMarketData;
import de.segoy.springboottradingdata.modelconverter.OptionChainDataToDbo;
import de.segoy.springboottradingdata.optionstradingservice.LastTradeDateBuilder;
import de.segoy.springboottradingdata.repository.PositionRepository;
import de.segoy.springboottradingweb.spxautotrade.service.order.SellOrderAutoTradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutoTradeKafkaHandler {
  private final LastTradeDateBuilder lastTradeDateBuilder;
  private final OptionChainDataToDbo optionChainDataToDBO;
  private final PositionRepository positionRepository;
  private final SellOrderAutoTradeService sellOrderAutoTradeService;
  private final TradeRuleSettingsConfig tradeRuleSettingsConfig;

  @KafkaListener(
      groupId = "${kafka.consumer.auto.group.id}",
      topics = "${kafka.names.topic.streams.optionChainData}")
  public void processChainData(OptionChainData message) {
    if (message.getLastTradeDate().equals(lastTradeDateBuilder.getDateLongFromToday())
        && message.getSymbol().equals(tradeRuleSettingsConfig.getTradeSymbol())) {
      optionChainDataToDBO.convertAndSave(message);
    }
  }
  @KafkaListener(
          groupId = "${kafka.consumer.auto.group.id}",
          topics = "${kafka.names.topic.standardMarketData}"
  )
  public void processLiveMarketData(StandardMarketData message) {
    if(String.valueOf(message.getTickerId()).endsWith(lastTradeDateBuilder.getShortenedDateStringFromToday())){
      positionRepository.findById((long)message.getTickerId()).ifPresent((position)->
      {
        if( message.getField().equals(TickType.ASK.field())){
          sellOrderAutoTradeService.sellPostionWhenPriceExceedsLimit(message.getPrice(), position);
        }
      });
    }
  }
}
