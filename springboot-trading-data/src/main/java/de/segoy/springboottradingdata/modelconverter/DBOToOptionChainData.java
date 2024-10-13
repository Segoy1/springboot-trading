package de.segoy.springboottradingdata.modelconverter;

import de.segoy.springboottradingdata.model.data.entity.OptionChainDataDBO;
import de.segoy.springboottradingdata.model.data.entity.OptionListDBO;
import de.segoy.springboottradingdata.model.data.kafka.KafkaOptionChainData;
import de.segoy.springboottradingdata.model.data.kafka.KafkaOptionListData;
import de.segoy.springboottradingdata.model.data.kafka.KafkaOptionMarketData;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class DBOToOptionChainData {

    @Transactional
    public KafkaOptionChainData toOptionChainData(OptionChainDataDBO optionChainDataDBO) {
        return KafkaOptionChainData.builder()
                .lastTradeDate(optionChainDataDBO.getLastTradeDate())
                .symbol(optionChainDataDBO.getSymbol())
                .calls(transformList(optionChainDataDBO.getCalls()))
                .puts(transformList(optionChainDataDBO.getPuts()))
                .build();
    }

    private KafkaOptionListData transformList(OptionListDBO optionList) {
        KafkaOptionListData listData = new KafkaOptionListData();

        optionList
                .getOptionList()
                .forEach(
                        (option) -> {
                            listData.put(
                                    option.getStrike(),
                                    KafkaOptionMarketData.builder()
                                            .tickerId(option.getTickerId().intValue())
                                            .strike(option.getStrike())
                                            .right(option.getRight())
                                            .symbol(option.getSymbol())
                                            .lastTradeDate(option.getLastTradeDate())
                                            .field(option.getField())
                                            .tickAttrib(option.getTickAttrib())
                                            .impliedVol(option.getImpliedVol())
                                            .delta(option.getDelta())
                                            .optPrice(option.getOptPrice())
                                            .pvDividend(option.getPvDividend())
                                            .gamma(option.getGamma())
                                            .vega(option.getVega())
                                            .undPrice(option.getUndPrice())
                                            .build());
                        });
        return listData;
    }
}
