package de.segoy.springboottradingdata.model.data.kafka;

import java.util.HashMap;
import java.util.Map;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class KafkaOptionListData extends KafkaDataType {

  private final Map<Double, KafkaOptionMarketData> options = new HashMap<>();

    public KafkaOptionListData(HashMap<Double, KafkaOptionMarketData> options) {
        this.options.putAll(options);
    }

    public void put(double strike, KafkaOptionMarketData option){
        options.put(strike, option);
    }
    public KafkaOptionMarketData get(double strike){
        return options.get(strike);
    }
    public Map<Double, Double> extractDeltas(){
        HashMap<Double,Double> deltas = new  HashMap<>();
        for(Map.Entry<Double, KafkaOptionMarketData> entry : options.entrySet()){
            deltas.put(entry.getKey(), entry.getValue().getDelta());
        }
        return deltas;
    }
    public int size(){
        return options.size();
    }
    public Map.Entry<Double, KafkaOptionMarketData> findClosestToDelta(double delta){
        return options.entrySet()
                .stream()
                .min((entry1, entry2) -> {
                    double deltaDiff1 = Math.abs(entry1.getValue().getDelta() - delta);
                    double deltaDiff2 = Math.abs(entry2.getValue().getDelta() - delta);
                    return Double.compare(deltaDiff1, deltaDiff2);
                })
                .orElse(null); // Return null if the map is empty
    }

}
