package de.segoy.springboottradingdata.model.data.kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OptionListData extends KafkaDataType {

  private final Map<Double, OptionMarketData> options = new HashMap<>();

    public OptionListData(HashMap<Double, OptionMarketData> options) {
        this.options.putAll(options);
    }

    public void put(double strike, OptionMarketData option){
        options.put(strike, option);
    }
    public OptionMarketData get(double strike){
        return options.get(strike);
    }
    public Map<Double, Double> extractDeltas(){
        HashMap<Double,Double> deltas = new  HashMap<>();
        for(Map.Entry<Double, OptionMarketData> entry : options.entrySet()){
            deltas.put(entry.getKey(), entry.getValue().getDelta());
        }
        return deltas;
    }
    public int size(){
        return options.size();
    }
    
    public Optional<Map.Entry<Double, OptionMarketData>> findClosestToDelta(double delta){
        return options.entrySet()
                .stream()
                .min((entry1, entry2) -> {
                    double deltaDiff1 = Math.abs(entry1.getValue().getDelta()) - delta;
                    double deltaDiff2 = Math.abs(entry2.getValue().getDelta()) - delta;
                    return Double.compare(deltaDiff1, deltaDiff2);
                });
    }

}
