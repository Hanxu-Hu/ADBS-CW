package ed.inf.adbs.minibase.operator;

//import com.sun.tools.javac.util.ArrayUtils;
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

    /**
     * this method construct a join operator, save related input arguments as private variables. Then create a new name
     * for the join relational atom, eg: "R_S" which is the name of relational atom joining "R" and "S".
     * @param left left child operator
     * @param right right child operator
     * @param leftAtom the relational atom of the left child operator
     * @param rightAtom the relational atom of the right child operator
     * @param comp comparison atoms
     */
    public JoinOperator(Operator left, Operator right, RelationalAtom leftAtom, RelationalAtom rightAtom, List<ComparisonAtom> comp){
        this.left = left;
        this.right = right;
        this.comp = comp;
        this.leftAtom = leftAtom;
        this.rightAtom = rightAtom;
        //this.leftTuple = left.getNextTuple();
        this.leftTuple = null;
        String leftName = leftAtom.getName();
        String rightName = rightAtom.getName();
        this.joinName = leftName+"_"+rightName;


    }

    /**
     * extends the method getNextTuple(), use two while-loop to create a simple nested loop join.
     * In the while-loop, use the check method in SelectCheck class used in SelectOpereator to determine whether the
     * current tuple satisfies the condition of comparison atoms.
     */
    @Override
    public Tuple getNextTuple() {
        if (leftTuple==null){
            leftTuple = left.getNextTuple();
        }
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
            right.reset();
            //System.out.println(leftTuple.toString());
            }
        return null;
    }

    /**
     * join the left relation and right relation to create a new relation.
     * to avoid the duplicated terms appears in the new relation atom, it use a hashmap to log the position of the common
     * term of two relation atoms. Then, remove the common terms appeared in the left atom based on the hashmap
     * Then, save the information of new relation to the catalog,
     * finally, join this two atoms as a new atom.
     */
    public RelationalAtom joinRelation() {
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
                    //System.out.println(i);
                    //System.out.println(j);
                }
                j += 1;
            }
            j=0;
            i += 1;
        }
        this.map = map;
        j=0;
        Catalog.INSTANCE.addSchema(leftAtom,rightAtom, map);
        if (map.keySet() != null) {
            for (Integer key : map.keySet()) {
                leftTerms.remove(key-j);
                j++;
            }
        }
        List<Term> newList = new ArrayList<>();
        newList.addAll(leftTerms);
        newList.addAll(rightTerms);
        String leftName = leftAtom.getName();
        String rightName = rightAtom.getName();
        String joinName = leftName + "_" + rightName;
        RelationalAtom joinRe = new RelationalAtom(joinName, newList);
        joinAtom = joinRe;
        return joinRe;
    }

    /**
     * this method join the left tuple and right tuple based on the mapping which created in the joinRelation() method to
     * log the duplicated position.
     * then return the joined tuple.
     * @param leftTuple the tuple of the left relation
     * @param rightTuple the tuple of the right relation
     */
    public Tuple joinMake(Tuple leftTuple, Tuple rightTuple){
        String[] sLeftTupleArray  = leftTuple.getArray();
        String[] sRightTupleArray  = rightTuple.getArray();
        List<String> leftListTuple= new LinkedList<String>(Arrays.asList(sLeftTupleArray));
        List<String> rightListTuple= new LinkedList<String>(Arrays.asList(sRightTupleArray));
        int j=0;
        if (map.keySet()!=null){
            for (Integer key: map.keySet()){
                String leftValue = leftListTuple.get(key-j);
                String rightValue = rightListTuple.get(map.get(key));
                if (leftValue.equals(rightValue)){
                    leftListTuple.remove(key-j);
                }
                else{
                    return null;
                }
                j++;//remove duplicate terms
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

    /**
     * extends reset()
     */
    @Override
    public void reset() {

    }
}
