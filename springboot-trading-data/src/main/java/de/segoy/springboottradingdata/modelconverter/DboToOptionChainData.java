package de.segoy.springboottradingdata.modelconverter;

import de.segoy.springboottradingdata.model.data.entity.OptionChainDbo;
import de.segoy.springboottradingdata.model.data.entity.OptionListDbo;
import de.segoy.springboottradingdata.model.data.kafka.KafkaOptionChainData;
import de.segoy.springboottradingdata.model.data.kafka.KafkaOptionListData;
import de.segoy.springboottradingdata.model.data.kafka.KafkaOptionMarketData;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class DboToOptionChainData {

    @Transactional
    public KafkaOptionChainData toOptionChainData(OptionChainDbo optionChainDbo) {
        return KafkaOptionChainData.builder()
                .lastTradeDate(optionChainDbo.getLastTradeDate())
                .symbol(optionChainDbo.getSymbol())
                .calls(transformList(optionChainDbo.getCalls()))
                .puts(transformList(optionChainDbo.getPuts()))
                .build();
    }

    private KafkaOptionListData transformList(OptionListDbo optionList) {
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
