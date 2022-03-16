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
    public ProjectionOperator(Operator child,RelationalAtom body, RelationalAtom head){
        this.head = head;
        this.body = body;
        this.child = child;
        this.buffer = new ArrayList<>();
    }

    public void SetChild(Operator child){
        this.child = child;

    }
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


    @Override
    public Tuple getNextTuple() {
        Tuple tuple = this.child.getNextTuple();
        if (tuple!=null){
            String sTuple = Process(tuple);


            boolean flag =this.buffer.contains(sTuple);
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
            result.setTuple(sTuple, "Q");
            return result;
            }

    return null;
    }

    @Override
    public void reset() {
        this.child.reset();

    }
}
