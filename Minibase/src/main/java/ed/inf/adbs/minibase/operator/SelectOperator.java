package ed.inf.adbs.minibase.operator;

import ed.inf.adbs.minibase.base.*;

import java.util.List;

public class SelectOperator extends Operator {
    private Operator child;
    private List<ComparisonAtom> comp;
    private RelationalAtom atom;


    public SelectOperator(Operator child, RelationalAtom rAtom, List<ComparisonAtom> compAtoms){
        this.child = child;
        List<Term> terms= rAtom.getTerms();                     //implicit equivalence predicate
        for (Term x:terms){
            boolean isVar = Variable.class.isInstance(x);
            if(isVar==false){
                ComparisonOperator op = ComparisonOperator.EQ;
                Term term1 = x;
                Term term2 =x;

                Variable var  = new Variable(x.toString());

                ComparisonAtom comp = new ComparisonAtom(var, term2, op);
                compAtoms.add(comp);
            }
        }
        this.comp =compAtoms;
        this.atom = rAtom;

    }
    public void SetChild(Operator child){
        this.child = child;

    }
    @Override

    public Tuple getNextTuple() {
        System.out.println(child);
        Tuple tuple = child.getNextTuple();
        SelectCheck check = new SelectCheck();
        while (tuple!=null){
            if (check.check(this.comp, this.atom, tuple)){
                return tuple;
            }
            else{
                tuple = child.getNextTuple();
            }
        }
        return null;
    }

    @Override
    public void reset() {
        this.child.reset();

    }
}
