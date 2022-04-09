package ed.inf.adbs.minibase.operator;

import ed.inf.adbs.minibase.base.RelationalAtom;
import ed.inf.adbs.minibase.base.Term;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class ProjectionOperator extends Operator{
    private Operator child;
    private List<String> buffer;
    private RelationalAtom body;
    private RelationalAtom head;
    private Boolean flag;

    /**
     * this method construct an instance of projection operator, save the input arguments as private variables.
     * @param child the child operator
     * @param body the relational atom of the query's body
     * @param head query's head
     * @param flag a boolean which indicate whether there is an aggregator in the query or not.
     */
    public ProjectionOperator(Operator child,RelationalAtom body, RelationalAtom head, Boolean flag){
        this.head = head;
        this.body = body;
        this.child = child;
        this.buffer = new ArrayList<>();
        this.flag = flag;
    }

    /**
     * this method input a tuple which is get from the child operator, then process the input tuple based on the head of
     * the query, projecting the tuple to the form of the head of the query. Specifically, use a hashmap to contain the
     * positional information of the relational atom's term, then use a list to contain the head term's position, and extract
     * the corresponding value of terms in tuple to form the final result.
     * @param tuple input tuple from the child operator
     * @return the string format of processed tuple
     */
    public String Process(Tuple tuple){
        List<Term> bodyTerms = this.body.getTerms();
        String sTuple = tuple.toString();
        String[] arrayTuple = sTuple.split(", ");
        List<Term> headTerms = this.head.getTerms();
        HashMap posMap = new HashMap<String, Integer>();   //position map  : eg: {a:0, b:1 }
        int i  =0;
        for (Term x : bodyTerms){
            String s =x.toString();
            posMap.put(s, i);
            i+=1;
        }
        List<Integer> posList = new ArrayList<>();
        for (Term x :headTerms){
            Object pos = posMap.get(x.toString());
            int p = (int) pos;
            posList.add(p);
        }

        List<String> res = new ArrayList<>();
        for (int p: posList){
            res.add(arrayTuple[p]);
        }

        String result = "";
        int j = 0;
        for (String x:res){
            String temp;
            if (j<(res.size()-1)){temp= x+", ";}
            else{temp = x;}
            result+=temp;
            j+=1;
        }
        return result;
    }


    /**
     * this extends getNextTuple() method of Operator class
     * it use a boolean variable private variable flag to indicate whether the head have aggregator or not.
     * Because when there is an aggregator, the projection operator should get all tuples, without the constraint of
     * distinct tuples, if it has an aggregator, the private variable flag is true, then it will not allow the buffer to
     * indicate the tuple which has already print.
     * @return
     */
    @Override
    public Tuple getNextTuple() {
        Tuple tuple = this.child.getNextTuple();
        if (tuple!=null){
            String sTuple = Process(tuple);
            boolean flag =this.buffer.contains(sTuple);
            if (this.flag){
                flag = false;
            }
            //System.out.println(this.buffer);
            while(flag){
                tuple = this.child.getNextTuple();
                if (tuple==null){
                    return null;
                }
                sTuple = Process(tuple);
                flag =this.buffer.contains(sTuple);
            }
            //System.out.println(string);

            this.buffer.add(sTuple);
            Tuple result = new Tuple();
            result.setTuple(sTuple, head.getName());
            return result;
            }
    return null;
    }

    /**
     * extends the method of reset()
     */
    @Override
    public void reset() {
        this.child.reset();
    }
}
