package ed.inf.adbs.minibase.operator;

import ed.inf.adbs.minibase.base.Aggregator;
import ed.inf.adbs.minibase.base.RelationalAtom;
import ed.inf.adbs.minibase.base.Term;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class AggregateOperator extends Operator {
    private final List<Tuple> tupleList = new ArrayList<Tuple>();
    private HashMap<String, Integer> finalMap = new HashMap<String, Integer>();
    private int pointer;
    private int pos;
    private int avg;
    private int sum;
    private String operation="";
    private final int headSize;
    private String aggName;
    private int maxSize=0;

    /**
     * this method construct an aggregator operator, it will first read all the related tuples and save them as private
     * variables, then process the operation of aggregator respectively based on the op argument, then save it as private variable.
     * It also indicate the position of the aggregate term appear in the head
     * of the query and save it. Then it creates a hashmap to indicate the mapping between term and related aggregator's
     * operation's result.
     * @param child child operator
     * @param head head of the query
     * @param op a string which indicate the type of the aggregator, "SUM" or "AVG"
     */
    public AggregateOperator(Operator child, RelationalAtom head, String op){

        operation = op;
        Tuple tuple = child.getNextTuple();
        while (tuple!=null){
            tupleList.add(tuple);
            System.out.println(tuple);
            System.out.println("tuple");
            tuple = new Tuple();
            tuple = child.getNextTuple();
        }
        List<Term> headTerms = head.getTerms();
        headSize = headTerms.size();
        int pos = 0;
        for(Term x: headTerms){
            if (x instanceof Aggregator) {
                break;
            }
            pos+=1;
        }
        if (headSize==1) {     //indicate that query only has an aggregator
            List<String> stringList = new ArrayList<>();
            Aggregator agg = (Aggregator) headTerms.get(0);
            aggName = agg.toString();
            for (Tuple t: tupleList){
                String stringTuple = t.toString();
                stringList.add(stringTuple);
            }
            List<Integer> intList = stringList.stream().map(Integer::parseInt).collect(Collectors.toList());
            System.out.println(intList);
            System.out.println("intList");
            if (op.equals("AVG")) {
                float a = (float) intList.stream().mapToInt(Integer::intValue).sum();
                float b = (float) intList.size();
                this.avg = Math.round(a/b);
            } else {
                int sum = intList.stream().mapToInt(Integer::intValue).sum();
                this.sum = sum;
            }
            this.maxSize = 1;
            return;
        }
        this.pos = pos;
        Aggregator agg = (Aggregator) headTerms.get(pos);
        aggName = agg.toString();
        HashMap<String,List<String>> map = new HashMap<String,List<String>>();
        assert tupleList != null;
        for (Tuple t : tupleList) {
            List<String> list = t.getList();
            List<String> tList = new ArrayList<>(list);
            String key = tList.get(pos);
            tList.remove(pos);

            String stringt = "";
            if (tList.size()>1){
                for (int i = 0; i < tList.size()-1; i++) {
                    stringt+=tList.get(i)+", ";
                }
                stringt+=tList.get(tList.size()-1);
            }
            else {
                stringt+=tList.get(0);
            }

            if (map.containsKey(stringt)) {
                List<String> var = map.get(stringt);
                var.add(key);
                map.put(stringt, var);
            } else {
                List<String> var = new ArrayList<>();
                var.add(key);
                map.put(stringt, var);
            }
        }
        HashMap<String,Integer> finalMap = new HashMap<String,Integer>();
        int lenKey = 0;

        for (String s:map.keySet()) {
            List<String> sList = map.get(s);
            List<Integer> intList = sList.stream().map(Integer::parseInt).collect(Collectors.toList());
            int sum = intList.stream().mapToInt(Integer::intValue).sum();
            if (op.equals("AVG")){
                float a = (float) sum;
                float b = (float) intList.size();
                int avg = Math.round(a/b);
                finalMap.put(s,avg);
            } else{
                finalMap.put(s,sum);
            }
            lenKey+=1;
        }
        this.finalMap = finalMap;
        maxSize = lenKey;
        pointer = 0;

    }

    /**
     * extended getNextTuple() method.
     * generate the tuple based on the mappings, operations results computed from the construct method.
     */
    @Override
    public Tuple getNextTuple() {
        if (pointer<maxSize){
            if (headSize==1) {
                String stringOp = "";
                if (operation.equals("AVG")){
                    stringOp = Integer.toString(avg);
                } else{
                    stringOp = Integer.toString(sum);
                }

                Tuple resTuple = new Tuple();
                resTuple.setTuple(stringOp,aggName);
                pointer+=1;
                return resTuple;

            } else{
                List<String> Keys = new ArrayList<String>(finalMap.keySet());
                System.out.println(finalMap);
                System.out.println("finamap");
                int agg = finalMap.get(Keys.get(pointer));
                String key = Keys.get(pointer);
                String[] arrayKey = key.split(", ");
                List<String> listK = Arrays.asList(arrayKey);
                List<String> listKey = new ArrayList<>();
                listKey.addAll(listK);
                String stringAgg = Integer.toString(agg);
                if (pos==listKey.size()){
                    listKey.add(stringAgg);
                } else {
                    listKey.add(pos,stringAgg);
                }

                String resString = "";
                for (int i = 0; i < listKey.size()-1 ; i++) {
                    resString+=listKey.get(i)+", ";
                }
                resString+=listKey.get(listKey.size()-1);
                Tuple resTuple = new Tuple();
                resTuple.setTuple(resString, aggName);
                pointer+=1;
                return resTuple;
            }
        }
        else{
            return null;
        }
    }

    /**
     * extended reset() method.
     */
    @Override
    public void reset() {
        pointer = 0;

    }
}
