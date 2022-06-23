import java.util.*;

public class Predictor {


    public Predictor(){
    }

    int newPredict(int lookback, List<Integer> data){
        int[] values = new int[lookback];
        int[] differences = new int[lookback-1];
        int pos = values.length-1;
        for (int i = data.size()-1; i > data.size() - 1 - lookback; i--) {
            //System.out.println(data.get(i));
            values[pos] = data.get(i);
            pos--;
        }

        for (int i = 0; i < values.length - 1; i++) {
            differences[i] = values[i+1] - values[i];
            //System.out.println("Difference between "+ values[i] +" and "+values[i+1]+" is: "+ differences[i]);
        }

        int rateOfChange = 0;

        for (int i = 0; i < differences.length-1; i++) {
            rateOfChange += differences[i+1] - differences[i];
            //System.out.println("Running total of ROT at point: " +i+ " is: " +rateOfChange);
        }

        //System.out.println("ROT:" + rateOfChange);

        rateOfChange /= differences.length - 1;

        //System.out.println("ROT*:" + rateOfChange);

        int newDiff = rateOfChange + differences[differences.length-1];

        int out = newDiff + values[values.length-1];
        if(out < 0) out = 0;

        return out;
    }

    void predictCall(int calls, int lookback, List<Integer> data, Map<Date, String> dataMap){
        List<Integer> output = new ArrayList<>();
        for (int i = 0; i < calls; i++) {
            int newData = newPredict(lookback, data);
            ArrayList<Date> temp = new ArrayList<>(dataMap.keySet());
            dataMap.put(temp.get(temp.size()-1).incrementDate(),Integer.toString(newData));
            data.add(newData);

        }
        //return output;
    }

    public static void main(String[] args) {
        List<Integer> Values = new ArrayList<Integer>();
        Values.add(1);
        Values.add(2);
        Values.add(3);
        Values.add(4);
        Values.add(1);
        Values.add(2);
        Values.add(3);
        Values.add(4);
        Values.add(1);
        Values.add(4);
        Values.add(9);
        Values.add(16);

        Predictor predictor = new Predictor();

        //System.out.println(predictor.newPredict(4, Values));

        ///predictor.predictCall(2,4,Values);

        //System.out.println(Values.get(Values.size()-1));


    }

}
