package ed.inf.adbs.minibase.operator;

import com.sun.tools.javac.util.ArrayUtils;
import ed.inf.adbs.minibase.Interpreter;
import ed.inf.adbs.minibase.base.ComparisonAtom;
import ed.inf.adbs.minibase.base.ComparisonOperator;
import ed.inf.adbs.minibase.base.RelationalAtom;
import ed.inf.adbs.minibase.base.Term;

import java.util.*;

import static java.util.Arrays.asList;

public class JoinOperator extends Operator{

    private RelationalAtom leftAtom;
    private RelationalAtom rightAtom;
    private Operator left;
    private Operator right;
    private List<ComparisonAtom> comp;
    private Tuple leftTuple;
    public RelationalAtom joinAtom;
    private HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
    private String joinName;

    public JoinOperator(Operator left, Operator right, RelationalAtom leftAtom, RelationalAtom rightAtom, List<ComparisonAtom> comp){
        this.left = left;
        this.right = right;
        this.comp = comp;
        this.leftAtom = leftAtom;
        this.rightAtom = rightAtom;
        this.leftTuple = left.getNextTuple();
        String leftName = leftAtom.getName();
        String rightName = rightAtom.getName();
        this.joinName = leftName+"_"+rightName;


    }

    @Override
    public Tuple getNextTuple() {
        while (leftTuple!=null){
            Tuple rightTuple = right.getNextTuple();
            while (rightTuple!=null){
                Tuple result = joinMake(leftTuple, rightTuple);
                if (result==null){
                    rightTuple = right.getNextTuple();
                    continue;

                }
                SelectCheck selectCheck = new SelectCheck();
                if(selectCheck.check(comp,joinAtom,result)){
                    return result;
                };
                rightTuple = right.getNextTuple();

            }
            this.leftTuple = left.getNextTuple();
            }
        return null;
    }
    public RelationalAtom merge(RelationalAtom leftAtom, RelationalAtom rightAtom){
        List<Term> leftterms = leftAtom.getTerms();
        List<Term> rightterms = rightAtom.getTerms();
        for (Term term: rightterms){

        }
        return null;
    }
    public void removeElement(String[] arr, int removedIdx) {
        System.arraycopy(arr, removedIdx + 1, arr, removedIdx, arr.length - 1 - removedIdx);
    }


    public RelationalAtom joinRelation() {
        //String[] sLeftTupleArray  = leftTuple.getArray();
        //String[] sRightTupleArray  = rightTuple.getArray();
        List<Term> leftTerms = leftAtom.getTerms();
        List<Term> rightTerms = rightAtom.getTerms();
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        int i = 0;
        int j = 0;
        for (Term leftTerm : leftTerms) {
            for (Term rightTerm : rightTerms) {
                String leftStringTerm = leftTerm.toString();
                String rightStringTerm = rightTerm.toString();
                if (leftStringTerm.equals(rightStringTerm)) {
                    map.put(i, j);
                    //if (!Objects.equals(sLeftTupleArray[i], sRightTupleArray[j])){
                    //    return null;//decide whether obey the implicit equivalence cross relation.

                }
                j += 1;
            }
            i += 1;
        }
        this.map = map;

        if (map.keySet() != null) {
            for (Integer key : map.keySet()) {
                leftTerms.remove(key);
            }
        }
        List<Term> newList = new ArrayList<>();
        newList.addAll(leftTerms);
        newList.addAll(rightTerms);
        String leftName = leftAtom.getName();
        String rightName = rightAtom.getName();
        String joinName = leftName + "_" + rightName;
        RelationalAtom joinRe = new RelationalAtom(joinName, newList);
        return joinRe;
    }
    public Tuple joinMake(Tuple leftTuple, Tuple rightTuple){
        String[] sLeftTupleArray  = leftTuple.getArray();
        String[] sRightTupleArray  = rightTuple.getArray();
        List<String> leftListTuple =  Arrays.asList(sLeftTupleArray);
        List<String> rightListTuple =  Arrays.asList(sRightTupleArray);
        if (map.keySet()!=null){
            for (Integer key: map.keySet()){
                leftListTuple.remove(key);  //remove duplicate terms
            }
        }

        List<String> newTupleList = new ArrayList<>();
        newTupleList.addAll(leftListTuple);
        newTupleList.addAll(rightListTuple);
        Tuple joinTuple = new Tuple();
        String sTuple = "";
        int flag = 0;

        while (flag<newTupleList.size()-1){
            sTuple+=newTupleList.get(flag)+", ";
            flag+=1;
        }
        sTuple+=newTupleList.get(flag);
        joinTuple.setTuple(sTuple,joinName);
        return joinTuple;


    }



    @Override
    public void reset() {

    }
}
