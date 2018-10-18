import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class VectorTerms {
    public Map<String,Double> termsValue = new HashMap<String,Double>();
    public double magnitude = 0.0;

    public void put(String key, Double value){
        termsValue.put(key,value);
    }

    public double get(String key){
        return termsValue.getOrDefault(key,0.0);
    }

    public void calculateMagnitude(){
        double tmp = 0.0;
        for (Map.Entry<String, Double> termEntry: termsValue.entrySet()) {
            tmp += Math.pow(termEntry.getValue(),2);
        }
        this.magnitude = Math.sqrt(tmp);
    }

    public void normalize(){
        if (termsValue.isEmpty()) return;
        double max = Collections.max(termsValue.values());
        termsValue.forEach((k,v) -> termsValue.put(k,v/max));
        calculateMagnitude();
    }
}
