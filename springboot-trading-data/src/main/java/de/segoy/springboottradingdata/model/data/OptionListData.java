package de.segoy.springboottradingdata.model.data;

import java.util.HashMap;
import java.util.Map;

public class OptionListData {

    private final Map<Double, OptionMarketData> options;

    public OptionListData() {
        options = new HashMap<>();
    }
    public OptionListData(HashMap<Double, OptionMarketData> options) {
        this.options = options;
    }

    public void put(double strike, OptionMarketData option){
        options.put(strike, option);
    }
    public OptionMarketData get(double strike){
        return options.get(strike);
    }
    public Map<Double, Double> getDeltas(){
        HashMap<Double,Double> deltas = new  HashMap<>();
        for(Map.Entry<Double, OptionMarketData> entry : options.entrySet()){
            deltas.put(entry.getKey(), entry.getValue().getDelta());
        }
        return deltas;
    }
    public Map.Entry<Double, OptionMarketData> findClosestToDelta(double delta){
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
