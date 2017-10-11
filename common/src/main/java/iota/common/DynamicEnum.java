package iota.common;

import java.util.ArrayList;

public class DynamicEnum {
    ArrayList<String> stringReps;
    ArrayList<Integer> numVals;

    public DynamicEnum(){
        stringReps = new ArrayList<>();
        numVals = new ArrayList<>();
    }

    public void addString(String str){
        stringReps.add(str);
        numVals.add(numVals.size());
    }

    public int getIntVal(String str){
        return 0;
    }

}
